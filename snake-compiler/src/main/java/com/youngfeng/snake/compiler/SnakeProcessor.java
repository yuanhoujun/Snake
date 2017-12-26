package com.youngfeng.snake.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.youngfeng.snake.annotations.EnableDragToClose;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Snake processor
 *
 * @author Scott Smith 2017-12-14 15:25
 */
@AutoService(Processor.class)
public class SnakeProcessor extends AbstractProcessor {
    private static final String FRAGMENT_TYPE = "android.app.Fragment";
    private static final String SUPPORT_FRAGMENT_TYPE = "android.support.v4.app.Fragment";
    private static final String ACTIVITY_TYPE = "android.app.Activity";
    private static final String LAYOUT_INFLATER_TYPE = "android.view.LayoutInflater";
    private static final String VIEWGROUP_TYPE = "android.view.ViewGroup";
    private static final String BUNDLE_TYPE = "android.os.Bundle";
    private Filer mFiler;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        for(Element element : env.getElementsAnnotatedWith(EnableDragToClose.class)) {
            if(!SuperficialValidation.validateElement(element)) continue;

            parseEnableDragToClose(element);
        }

        return false;
    }

    private void parseEnableDragToClose(Element element) {
        boolean hasError = isBindingInWrongClass(element, EnableDragToClose.class);
        if(hasError) return;

        TypeMirror elementType = element.asType();
        // Only process Fragment or its child class.
        if(!isSubtypeOfFragment(elementType)) return;

        if(isFinalClass(element)) {
            error(element, "%s can't be used in a final class, %s",
                    EnableDragToClose.class.getSimpleName(), element.getSimpleName().toString());
            return;
        }

        // Do nothing if it has been processed!!!
        if(elementType.toString().endsWith("_SnakeProxy")) return;

        TypeElement typeElement = (TypeElement) element;
        String packageName = getPackageName(typeElement);
        String simpleName = element.getSimpleName().toString();
        TypeName typeName = TypeName.get(elementType);

        MethodSpec.Builder bindMethodBuilder = null;
        List<? extends Element> members = mElementUtils.getAllMembers(typeElement);

        for(Element member : members) {
            if(member instanceof ExecutableElement) {
                if(isOnCreateView((ExecutableElement) member)) {
                    ExecutableElement executableElement = (ExecutableElement) member;
                    bindMethodBuilder = MethodSpec.overriding(executableElement);

                    List<? extends VariableElement> parameters = executableElement.getParameters();

                    if(null != parameters && !parameters.isEmpty()) {
                        String callSuper = "View view = super.onCreateView(%s, %s, %s)";
                        for(VariableElement variableElement : parameters) {
                            String variableTypeName = variableElement.getSimpleName().toString();
                            callSuper = callSuper.replaceFirst("%s", variableTypeName);
                        }
                        bindMethodBuilder.addStatement(callSuper).addCode("\n");
                    }

                    break;
                }
            }
        }

        if(null != bindMethodBuilder && null != mFiler) {
            ClassName snakeHackLayoutClass = ClassName.get("com.youngfeng.snake.view", "SnakeHackLayout");
            TypeElement snakeHackLayoutTypeElement = mElementUtils.getTypeElement(snakeHackLayoutClass.toString());
            ClassName snakeClass = ClassName.get("com.youngfeng.snake", "Snake");
            TypeElement snakeTypeElement = mElementUtils.getTypeElement(snakeClass.toString());

            MethodSpec method = bindMethodBuilder
                    .addStatement("$T enableDragToClose = getClass().getAnnotation($T.class)",
                            EnableDragToClose.class, EnableDragToClose.class)
                    .addStatement("if(null != enableDragToClose && !enableDragToClose.value()) return view")
                    .addCode("\n")
                    .addStatement("mSnakeLayout = $T.getLayout(getActivity(), view, true)",
                            snakeHackLayoutTypeElement.asType())
                    .addStatement("$T.openDragToCloseForFragment(mSnakeLayout, this)", snakeTypeElement.asType())
                    .addCode("\n")
                    .addStatement("return mSnakeLayout")
                    .build();


            FieldSpec field = FieldSpec.builder(TypeName.get(snakeHackLayoutTypeElement.asType()),
                    "mSnakeLayout", Modifier.PRIVATE).build();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.CHINA);
            String dateStr = dateFormat.format(new Date());
            TypeSpec typeSpec = TypeSpec.classBuilder(simpleName + "_SnakeProxy")
                                        .addJavadoc("Use this class to implement drag to close current fragment\n")
                                        .addJavadoc("\n")
                                        .addJavadoc("@author Scott Smith $L\n", dateStr)
                                        .addModifiers(Modifier.PUBLIC)
                                        .superclass(typeName)
                                        .addMethod(method)
                                        .addMethod(buildMethodEnableDragToClose())
                                        .addField(field)
                                        .build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                    .addFileComment("Generated code from Snake. Do not edit!").build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private MethodSpec buildMethodEnableDragToClose() {
        ParameterSpec parameter = ParameterSpec.builder(Boolean.class, "enable").build();

        ClassName snakeConfigExceptionClass = ClassName.get("com.youngfeng.snake.config", "SnakeConfigException");
        TypeElement snakeConfigExceptionTypeElement = mElementUtils.getTypeElement(snakeConfigExceptionClass.toString());


        return MethodSpec.methodBuilder("enableDragToClose")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameter)
                .addStatement("$T enableDragToClose = getClass().getAnnotation($T.class)", EnableDragToClose.class, EnableDragToClose.class)
                .addCode("if(enable) {\n")
                .addCode("\tif(null == enableDragToClose || !enableDragToClose.value()) {\n")
                .addStatement("\t\tthrow new $T($S + getClass().getName() + $S)",
                        snakeConfigExceptionTypeElement.asType(),
                        "If you want to dynamically turn the slide-off feature on or off, add the EnableDragToClose annotation to ",
                        " and set to true")
                .addCode("\t}\n")
                .addCode("}\n\n")
                .addCode("if(null != mSnakeLayout) { \n")
                .addCode("\tmSnakeLayout.ignoreDragEvent(!enable);\n")
                .addCode("}\n")
                .returns(TypeName.VOID)
                .build();
    }

    // Depend whether the method's name is onCreateView
    private boolean isOnCreateView(ExecutableElement element) {
        if(!"onCreateView".equals(element.getSimpleName().toString())) return false;

        List<? extends VariableElement> parameters = element.getParameters();

        // Diff parameters, need totally idenditify .
        if(null == parameters || parameters.size() != 3) return false;
        for(VariableElement parameter : parameters) {
            if(!isTypeEqual(parameter.asType(), LAYOUT_INFLATER_TYPE)
            && !isTypeEqual(parameter.asType(), VIEWGROUP_TYPE)
            && !isTypeEqual(parameter.asType(), BUNDLE_TYPE)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSubtypeOfFragment(TypeMirror typeMirror) {
        return isSubtypeOfType(typeMirror, FRAGMENT_TYPE) || isSubtypeOfType(typeMirror, SUPPORT_FRAGMENT_TYPE);
    }

    private boolean isFinalClass(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        if(null == modifiers || modifiers.isEmpty()) return false;

        for(Modifier modifier : modifiers) {
            if(Modifier.FINAL == modifier) {
                return true;
            }
        }

        return false;
    }

    private String getPackageName(TypeElement typeElement) {
        return mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    private boolean isBindingInWrongClass(Element element, Class<? extends Annotation> annotationClass) {
        boolean hasError = false;

        TypeMirror elementType = element.asType();

        if(!isSubtypeOfType(elementType, FRAGMENT_TYPE) && !isSubtypeOfType(elementType, SUPPORT_FRAGMENT_TYPE)
                && !isSubtypeOfType(elementType, ACTIVITY_TYPE)) {
            error(element, "%s only can be used in %s or %s or their child class ",
                    EnableDragToClose.class.getName(), "Activity", "Fragment");
            hasError = true;
        }

        return hasError;
    }

    static boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (isTypeEqual(typeMirror, otherType)) {
            return true;
        }

        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }

        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }

        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }

        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }

        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTypeEqual(TypeMirror typeMirror, String otherType) {
        return otherType.equals(typeMirror.toString());
    }

    public Set<Class<? extends Annotation>> getSupportAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(EnableDragToClose.class);

        return annotations;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();

        for(Class<? extends Annotation> annotation: getSupportAnnotations()) {
            types.add(annotation.getCanonicalName());
        }

        return types;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void note(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.NOTE, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        processingEnv.getMessager().printMessage(kind, message, element);
    }
}

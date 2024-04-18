//package com.seai.spring.config;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.Version;
//import com.fasterxml.jackson.databind.AnnotationIntrospector;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.introspect.Annotated;
//import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
//import com.seai.marine.user.model.AsyncApiEnum;
//import io.swagger.v3.core.converter.AnnotatedType;
//import io.swagger.v3.oas.models.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.SneakyThrows;
//import org.springdoc.core.customizers.PropertyCustomizer;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Component
//public class EnumApiDefinitionCustomizer
//        implements PropertyCustomizer {
//
//    private final ObjectMapper objectMapper;
//
//    public EnumApiDefinitionCustomizer() {
//        this.objectMapper = new ObjectMapper();
//    }
//
//    @Override
//    @SneakyThrows
//    public Schema customize(Schema property, AnnotatedType type) {
//        if (isEnum(type)) {
//            property.setEnum(
//                    Arrays.stream(((JavaType) type.getType()).getRawClass().getEnumConstants())
//                            .map(v-> (AsyncApiEnum)v)
//                            .map(v-> new AsyncApiEnumObject(v.getName(), v.getLabel()))
//                            .map(this::writeValueAsString)
//                            .toList());
//        }
//
//        return property;
//    }
//
//    private String writeValueAsString(Object o) {
//        try {
//            return this.objectMapper.writeValueAsString(o);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private boolean isEnum(AnnotatedType type) {
//        return type.getType() instanceof JavaType t
//                && t.isEnumType();
//    }
//
//    @Data
//    @AllArgsConstructor
//    static class AsyncApiEnumObject {
//        String name;
//        String label;
//    }
//
//    static class DynamicEnumAnnotationIntrospector extends AnnotationIntrospector {
//
//        @Override
//        public Version version() {
//            return new Version(1, 0, 0, "Dynamic enum object", "com.seai", "jackson.dynamic.enum");
//        }
//
//        @Override
//        public JsonFormat.Value findFormat(Annotated memberOrClass) {
//            final Class<?> rawType = memberOrClass.getRawType();
//            if (rawType.isEnum()) {
//                return JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY);
//            }
//
//            return super.findFormat(memberOrClass);
//        }
//    }
//}

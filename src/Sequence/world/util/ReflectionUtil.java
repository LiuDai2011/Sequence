package Sequence.world.util;

import arc.func.Cons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    public static <T> FieldEntry getField(Class<T> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            return new FieldEntry(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> MethodEntry getMethod(Class<T> clazz, String name, Class<?> ...argTypes) {
        try {
            Method method = clazz.getDeclaredMethod(name, argTypes);
            return new MethodEntry(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static class FieldEntry {
        public final Field field;

        public FieldEntry(Field field) {
            this.field = field;
        }

        public FieldEntry setAccessible(boolean flag) {
            field.setAccessible(flag);
            return this;
        }

        public FieldEntry run(Cons<FieldEntry> cons) {
            cons.get(this);
            return this;
        }

        public Object get(Object obj) {
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public FieldEntry set(Object obj, Object value) {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
    }

    public static class MethodEntry {
        public final Method method;

        public MethodEntry(Method method) {
            this.method = method;
        }

        public MethodEntry setAccessible(boolean flag) {
            method.setAccessible(flag);
            return this;
        }

        public MethodEntry run(Cons<MethodEntry> cons) {
            cons.get(this);
            return this;
        }
    }
}

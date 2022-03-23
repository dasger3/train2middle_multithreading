package task5.exception;

public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException(String className, String title) {
        super("No " + className + " with " + title + " name found");
    }
    public ObjectNotFoundException(String className, Long id) {
        super("No " + className + " with " + id + " id found");
    }
}

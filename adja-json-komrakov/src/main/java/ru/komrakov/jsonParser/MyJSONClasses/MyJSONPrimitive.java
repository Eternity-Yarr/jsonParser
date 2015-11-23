package ru.komrakov.jsonParser.MyJSONClasses;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.komrakov.jsonParser.JSONPrimitiveClass;


/**
 * Created by User on 08.11.2015.
 */
public class MyJSONPrimitive extends JSONPrimitiveClass {

    public MyJSONPrimitive(Object o) {
        super(o);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    //FIXME: Cleanup?
    /*
    "equals()" notes:
    The reason to use getClass is to ensure the symmetric property of the equals contract. From equals' JavaDocs:

    It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
    By using instanceof, it's possible to not be symmetric. Consider the example: Dog extends Animal. Animal's equals
    does an instanceof check of Animal. Dog's equals does an instanceof check of Dog. Give Animal a and Dog d (with other fields the same):

    a.equals(d) --> true
    d.equals(a) --> false
    This violates the symmetric property.

    To strictly follow equal's contract, symmetry must be ensured, and thus the class needs to be the same.

    about getClass():
    Implementations using getClass() do not allow comparison of sub- with superclass objects,
    not even when the subclass does not add any fields and would not even want to override equals()

    more detail: http://stackoverflow.com/questions/596462/any-reason-to-prefer-getclass-over-instanceof-when-generating-equals
    http://www.javaworld.com/article/2072762/java-app-dev/object-equality.html
     */

    //EqualsBuilder and HashBuilder usage
    //discussion here: http://stackoverflow.com/questions/10912646/hashcodebuilder-and-equalsbuilder-usage-style

    //perfomance notes:
    //....4 objects with different equals implementations. eclipse generated, equalsbuilder.append,
    // equalsbuilder.reflection, and pojomatic annotations. The baseline was eclipse. equalsbuilder.append took 3.7x.
    // pojomatic took 5x. reflection based took 25.8x.
    //http://stackoverflow.com/questions/5038204/apache-commons-equals-hashcode-builder


    /* manual implementation

    private static final int NULL_HASHCODE = 73;
    private static final int MAX_HASHCODE_LENGTH = 50;

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }

        if (o == null){
            return false;
        }

        if (this.getClass() != o.getClass()){
            return false;
        }

        if (this.value == ((MyJSONPrimitive)o).value){
            return true;
        };

        return false;
    }

    @Override
    public int hashCode(){

        if (value == null){
            return NULL_HASHCODE;
        }

        String strRepresentation = value.toString();
        char[] symbols = strRepresentation.toCharArray();

        int hashCode = 0;
        int hashCodeLengthControl = 0;
        for(char c: symbols){
            hashCode = hashCode*10 + (int)c;

            hashCodeLengthControl++;
            if (hashCodeLengthControl > MAX_HASHCODE_LENGTH){
                break;
            }
        }
        return hashCode;
    }
    */




}

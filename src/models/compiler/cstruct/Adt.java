package models.compiler.cstruct;

import models.compiler.InvalidExpressionException;
import models.compiler.fileanalyzer.Token;
import models.compiler.fileanalyzer.TokenListFactory;
import models.compiler.fileanalyzer.TokenType;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * User: Javier Isoldi
 * Date: 4/29/13
 * Time: 1:12 PM
 */
public class Adt {
    private String name; // name of the Adt
    private List<Attribute> attributes; // attributes of the Adt

    public Adt(String name, List<Attribute> attributes, Map<String, Number> globalAttributes) {
        this.name = name;
        this.attributes = attributes;
        Map<String, Number> globalAttributes1 = globalAttributes;
    }

    public static List<Attribute> readAttributesFromBlock(Token token, List<Adt> adts, Map<String, Number> globalAttributes) throws IOException, InvalidExpressionException {
        String adtBody = token.getValue();
        String adtBodyWithoutBrackets = adtBody.substring(1, adtBody.length() - 1);
        TokenListFactory tokenListFactory = new TokenListFactory(globalAttributes);
        Iterator<Token> tokenIterator;
        tokenIterator = tokenListFactory.getTokenFileFromCFile(new StringReader(adtBodyWithoutBrackets)).iterator();

        List<Attribute> attributeList = new LinkedList<>();

        while (tokenIterator.hasNext()) {
            token = tokenIterator.next();
            if (token.getValue().equals("struct")) //cuando es autoreferenciado hay q sacar el "struct"
                token = tokenIterator.next();
            for (Attribute attribute : readAttribute(token, tokenIterator, adts)) {
                attributeList.add(attribute);
            }
        }

        return attributeList;
    }

    private static List<Attribute> readAttribute(Token token, Iterator<Token> tokenIterator, List<Adt> adts) throws InvalidExpressionException {
        if (token.getType() == TokenType.BASIC_TYPE) {
            String type = token.getValue();
            token = tokenIterator.next();
            return readAttributeWithType(type, token, tokenIterator);
        } else if (token.getType() == TokenType.IDENTIFIER) {
            for (Adt adt : adts) {
                if (token.getValue().equals(adt.getName())) {
                    String type = token.getValue();
                    token = tokenIterator.next();
                    return readAttributeWithType(type, token, tokenIterator);
                }
            }
        }
        throw new InvalidExpressionException(token.toString());

    }

    private static List<Attribute> readAttributeWithType(String type, Token token, Iterator<Token> tokenIterator) throws InvalidExpressionException {
        ArrayList<Attribute> attributes = new ArrayList<>();
        while (true) {
            if (token.getType() == TokenType.IDENTIFIER) {
                String name = token.getValue();
                token = tokenIterator.next();
                if (token.getType() == TokenType.SENTENCE_END) {
                    attributes.add(new Attribute(type, name, false, 0));
                    return attributes;
                } else if (token.getType() == TokenType.SQUARE_BRACKET_BLOCK) {
                    String arrayCap = token.getValue();
                    token = tokenIterator.next();
                    attributes.add(new Attribute(type, name, true, parseInt(arrayCap)));
                    if (token.getType() == TokenType.SENTENCE_END) {
                        return attributes;
                    } else if (token.getType() == TokenType.COMMA_OPERATOR) {
                        token = tokenIterator.next();
                    } else {
                        throw new InvalidExpressionException(token.toString());
                    }
                } else if (token.getType() == TokenType.COMMA_OPERATOR) {
                    token = tokenIterator.next();
                } else {
                    throw new InvalidExpressionException(token.toString());
                }
            } else {
                throw new InvalidExpressionException(token.toString());
            }
        }
    }

    public static int parseInt(String arraycap) {
        arraycap = arraycap.substring(1, arraycap.length() - 1);
        if (arraycap.contains("."))
            arraycap = arraycap.substring(0, arraycap.length() - 2);
        return Integer.parseInt(arraycap);
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String toString() {
        String result = ("\tThe name is : " + name);
        if (attributes != null) {
            result += ("\n\tThe variables are: ");
            for (Attribute attribute : attributes) {
                result += "\n\t" + (attribute.toString());
            }
        }
        return result;
    }

}

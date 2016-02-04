/**
 * This software is copyright (c) 2012-2016 by
 *  - Institut fuer Deutsche Sprache (http://www.ids-mannheim.de)
 * This is free software. You can redistribute it
 * and/or modify it under the terms described in
 * the GNU General Public License v3 of which you
 * should have received a copy. Otherwise you can download
 * it from
 *
 *   http://www.gnu.org/licenses/gpl-3.0.txt
 *
 * @copyright Institut fuer Deutsche Sprache (http://www.ids-mannheim.de)
 *
 * @license http://www.gnu.org/licenses/gpl-3.0.txt
 *  GNU General Public License v3
 */
package eu.clarin.sru.client.fcs;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import eu.clarin.sru.client.SRUClientException;


/**
 * An implementation of a Data View parser that stores the content of a Data
 * View in DOM representation.
 *
 * @see DataViewGenericDOM
 */
public class DataViewParserGenericDOM implements DataViewParser {
    private static class TransformHelper {
        private final DocumentBuilder builder;
        private final Transformer transformer;


        private TransformHelper(DocumentBuilder builder,
                Transformer transformer) {
            if (builder == null) {
                throw new NullPointerException("builder == null");
            }
            this.builder = builder;
            if (transformer == null) {
                throw new NullPointerException("transformer == null");
            }
            this.transformer = transformer;
        }


        private Document transform(XMLStreamReader reader)
                throws XMLStreamException, TransformerException {
            // parse STAX to DOM fragment
            Document document = builder.newDocument();
            DOMResult result = new DOMResult(document);
            transformer.transform(new StAXSource(reader), result);
            return document;
        }


        private void reset() {
            builder.reset();
            transformer.reset();
        }
    } // private class TransformHelper
    private final ThreadLocal<TransformHelper> transformHelper;

    public DataViewParserGenericDOM() {
        final DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        final TransformerFactory transformerFactory =
                TransformerFactory.newInstance();
        this.transformHelper = new ThreadLocal<TransformHelper>() {
            @Override
            protected TransformHelper initialValue() {
                try {
                    return new TransformHelper(builderFactory.newDocumentBuilder(),
                                   transformerFactory.newTransformer());
                } catch (TransformerConfigurationException e) {
                    throw new InternalError("unexpected error creating new transformer");
                } catch (ParserConfigurationException e) {
                    throw new InternalError("unexpected error creating new document builder");
                }
            }
        };


    }


    @Override
    public boolean acceptType(String type) {
        return true;
    }


    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }


    @Override
    public DataView parse(XMLStreamReader reader, String type, String pid,
            String ref) throws XMLStreamException, SRUClientException {
        final TransformHelper helper = transformHelper.get();
        try {
            final Document document = helper.transform(reader);
            final NodeList children = document.getChildNodes();
            if ((children != null) && (children.getLength() > 0)) {
                return new DataViewGenericDOM(type, pid, ref, document);
            } else {
                throw new SRUClientException("element <DataView> does not "
                        + "contain any nested elements");
            }
        } catch (TransformerException e) {
            throw new SRUClientException("error while parsing dataview", e);
        } finally {
            helper.reset();
        }
    }

} // class DataViewParserGenericDOM

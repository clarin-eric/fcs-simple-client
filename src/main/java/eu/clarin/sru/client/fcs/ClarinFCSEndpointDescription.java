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

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import eu.clarin.sru.client.SRUExtraResponseData;


/**
 * CLARIN-FCS endpoint description holder class.
 */
public class ClarinFCSEndpointDescription implements Serializable,
        SRUExtraResponseData {
    private static final long serialVersionUID = 9061228238942949036L;
    private static final QName ROOT_ELEMENT =
            new QName("http://clarin.eu/fcs/endpoint-description",
                      "EndpointDescription");
    private final int version;
    private final List<URI> capabilites;
    private final List<DataView> supportedDataViews;
    private final List<Layer> supportedLayers;
    private final List<ResourceInfo> resources;


    ClarinFCSEndpointDescription(int version, List<URI> capabilites,
            List<DataView> supportedDataViews, List<Layer> supportedLayers,
            List<ResourceInfo> resources) {
        this.version = version;
        if ((capabilites != null) && !capabilites.isEmpty()) {
            this.capabilites = Collections.unmodifiableList(capabilites);
        } else {
            this.capabilites = Collections.emptyList();
        }
        if ((supportedDataViews != null) && !supportedDataViews.isEmpty()) {
            this.supportedDataViews =
                    Collections.unmodifiableList(supportedDataViews);
        } else {
            this.supportedDataViews = Collections.emptyList();
        }
        if ((supportedLayers != null) && !supportedLayers.isEmpty()) {
            this.supportedLayers =
                    Collections.unmodifiableList(supportedLayers);
        } else {
            this.supportedLayers = Collections.emptyList();
        }
        if ((resources != null) && !resources.isEmpty()) {
            this.resources = Collections.unmodifiableList(resources);
        } else {
            this.resources = Collections.emptyList();
        }
    }


    @Override
    public QName getRootElement() {
        return ROOT_ELEMENT;
    }


    /**
     * Get the version of this endpoint description.
     *
     * @return the version of the endpoint description
     */
    public int getVersion() {
        return version;
    }


    /**
     * Get the list of capabilities supported by this endpoint. The list
     * contains the appropriate URIs defined by the CLARIN-FCS specification to
     * indicate support for certain capabilities.
     *
     * @return the list of capabilities supported by this endpoint
     */
    public List<URI> getCapabilities() {
        return capabilites;
    }


    /**
     * Get the list of data views supported by this endpoint.
     *
     * @return the list of data views supported by this endpoint
     */
    public List<DataView> getSupportedDataViews() {
        return supportedDataViews;
    }


    /**
     * Get the list of data views supported by this endpoint.
     *
     * @return the list of data views supported by this endpoint
     */
    public List<Layer> getSupportedLayers() {
        return supportedLayers;
    }


    /**
     * Get the list of top-level resources of this endpoint.
     *
     * @return the list of top-level resources of this endpoint
     */
    public List<ResourceInfo> getResources() {
        return resources;
    }


    /**
     * This class implements a description of a data view supported by the endpoint.
     */
    public static final class DataView implements Serializable {
        /**
         * Enumeration to indicate the delivery policy of a data view.
         */
        public enum DeliveryPolicy {
            /**
             * The data view is sent automatically  by the endpoint.
             */
            SEND_BY_DEFAULT,
            /**
             * A client must explicitly request the endpoint.
             */
            NEED_TO_REQUEST;
        } // enum PayloadDelivery
        private static final long serialVersionUID = -5628565233032672627L;
        private final String identifier;
        private final String mimeType;
        private final DeliveryPolicy deliveryPolicy;


        /**
         * Constructor. <em>Internal use only!</em>
         */
        DataView(String identifier, String mimeType,
                DeliveryPolicy deliveryPolicy) {
            if (identifier == null) {
                throw new NullPointerException("identifier == null");
            }
            if (identifier.isEmpty()) {
                throw new IllegalArgumentException("identifier is empty");
            }
            this.identifier = identifier;

            if (mimeType == null) {
                throw new NullPointerException("mimeType == null");
            }
            if (mimeType.isEmpty()) {
                throw new IllegalArgumentException("mimeType is empty");
            }
            this.mimeType = mimeType;

            if (deliveryPolicy == null) {
                throw new NullPointerException("deliveryPolicy == null");
            }
            this.deliveryPolicy = deliveryPolicy;
        }


        /**
         * Get the identifier of this data view.
         *
         * @return the identifier of the data view
         */
        public String getIdentifier() {
            return identifier;
        }


        /**
         * Get the MIME type of this data view.
         *
         * @return the MIME type of this data view
         */
        public String getMimeType() {
            return mimeType;
        }


        /**
         * Get the delivery policy for this data view.
         *
         * @return the delivery policy of this data view
         * @see DeliveryPolicy
         */
        public DeliveryPolicy getDeliveryPolicy() {
            return deliveryPolicy;
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName());
            sb.append("[");
            sb.append("identifier=").append(identifier);
            sb.append(", mimeType=").append(mimeType);
            sb.append("]");
            return sb.toString();
        }
    } // class DataView


    public static final class Layer implements Serializable {
        /**
         * Enumeration to indicate the content encoding of a layer.
         */
        public enum ContentEncoding {
            /**
             * The layer is encoded as content values.
             */
            VALUE,
            /**
             * The layer is encoded just as segment units.
             */
            EMPTY
        }
        private static final long serialVersionUID = 6641490182609459912L;
        private final String identifier;
        private final URI resultId;
        private final String layerType;
        private final ContentEncoding encoding;
        private final String qualifier;
        private final String altValueInfo;
        private final URI altValueInfoURI;


        /**
         * Constructor. <em>Internal use only!</em>
         */
        Layer(String identifier, URI resultId, String layerType,
                ContentEncoding encoding, String qualifier,
                String altValueInfo, URI altValueInfoURI) {
            if (identifier == null) {
                throw new NullPointerException("identifier == null");
            }
            if (identifier.isEmpty()) {
                throw new IllegalArgumentException("identifier is empty");
            }
            this.identifier = identifier;

            if (resultId == null) {
                throw new NullPointerException("resultId == null");
            }
            this.resultId = resultId;

            if (layerType == null) {
                throw new NullPointerException("layerType == null");
            }
            if (layerType.isEmpty()) {
                throw new IllegalArgumentException("layerType is empty");
            }
            this.layerType = layerType;
            this.encoding = (encoding != null)
                    ? encoding
                    : ContentEncoding.VALUE;
            this.qualifier = qualifier;
            this.altValueInfo = altValueInfo;
            this.altValueInfoURI = altValueInfoURI;
        }


        /**
         * Get the identifier of this layer
         *
         * @return the identifier of the layer
         */
        public String getIdentifier() {
            return identifier;
        }


        /**
         * Get the result URI of this layer
         *
         * @return the result URI of the layer
         */
        public URI getResultId() {
            return resultId;
        }


        /**
         * Get the layer type of this layer
         *
         * @return the layer type of the layer
         */
        public String getLayerType() {
            return layerType;
        }


        /**
         * Get the content encoding of this layer
         *
         * @return the content encoding of the layer
         */
        public ContentEncoding getEncoding() {
            return encoding;
        }


        /**
         * Get the qualifier of this layer
         *
         * @return the qualifier of the layer or <code>null</code> if none
         */
        public String getQualifier() {
            return qualifier;
        }


        /**
         * Get the alternative value information of this layer
         *
         * @return the alternative value information of the layer or
         *         <code>null</code> if none
         */
        public String getAltValueInfo() {
            return altValueInfo;
        }


        /**
         * Get the alternative value information URI of this layer
         *
         * @return the alternative value information URI of the layer or
         *         <code>null</code> if none
         */
        public URI getAltValueInfoURI() {
            return altValueInfoURI;
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName());
            sb.append("[");
            sb.append("identifier=").append(identifier);
            sb.append(", result-id=").append(resultId);
            sb.append(", layer-type=").append(layerType);
            if (encoding != null) {
                sb.append(", encoding=").append(encoding);
            }
            if (qualifier != null) {
                sb.append(", qualifier=").append(qualifier);
            }
            if (altValueInfo != null) {
                sb.append(", alt-value-info=").append(altValueInfo);
            }
            if (altValueInfoURI != null) {
                sb.append(", alt-value-info-uri=").append(altValueInfoURI);
            }
            sb.append("]");
            return sb.toString();
        }
    }


    /**
     * This class implements a description of a resource available at an
     * endpoint.
     */
    public static final class ResourceInfo implements Serializable {
        private static final long serialVersionUID = 1046130188435071544L;
        private final String pid;
        private final Map<String, String> title;
        private final Map<String, String> description;
        private final String landingPageURI;
        private final List<String> languages;
        private final List<DataView> availableDataViews;
        private final List<Layer> availableLayers;
        private final List<ResourceInfo> subResources;


        /**
         * Constructor. <em>Internal use only!</em>
         */
        ResourceInfo(String pid, Map<String, String> title,
                Map<String, String> description, String landingPageURI,
                List<String> languages, List<DataView> availableDataViews,
                List<Layer> availableLayers,
                List<ResourceInfo> subResources) {
            if (pid == null) {
                throw new NullPointerException("pid == null");
            }
            this.pid = pid;

            if (title == null) {
                throw new NullPointerException("title == null");
            }
            if (title.isEmpty()) {
                throw new IllegalArgumentException("title is empty");
            }
            this.title = Collections.unmodifiableMap(title);
            if ((description != null) && !description.isEmpty()) {
                this.description = Collections.unmodifiableMap(description);
            } else {
                this.description = null;
            }

            this.landingPageURI = landingPageURI;
            if (languages == null) {
                throw new NullPointerException("languages == null");
            }
            if (languages.isEmpty()) {
                throw new IllegalArgumentException("languages is empty");
            }
            this.languages = languages;

            if (availableDataViews == null) {
                throw new IllegalArgumentException("availableDataViews == null");
            }
            this.availableDataViews =
                    Collections.unmodifiableList(availableDataViews);

            if ((availableLayers != null) && !availableLayers.isEmpty()) {
                this.availableLayers =
                        Collections.unmodifiableList(availableLayers);
            } else {
                this.availableLayers = Collections.emptyList();
            }

            if ((subResources != null) && !subResources.isEmpty()) {
                this.subResources = Collections.unmodifiableList(subResources);
            } else {
                this.subResources = null;
            }
        }


        /**
         * Get the persistent identifier of this resource.
         *
         * @return a string representing the persistent identifier of this resource
         */
        public String getPid() {
            return pid;
        }


        /**
         * Determine, if this resource has sub-resources.
         *
         * @return <code>true</code> if the resource has sub-resources,
         *         <code>false</code> otherwise
         */
        public boolean hasSubResources() {
            return subResources != null;
        }


        /**
         * Get the title of this resource.
         *
         * @return a Map of titles keyed by language code
         */
        public Map<String, String> getTitle() {
            return title;
        }


        /**
         * Get the title of the resource for a specific language code.
         *
         * @param language
         *            the language code
         * @return the title for the language code or <code>null</code> if no title
         *         for this language code exists
         */
        public String getTitle(String language) {
            return title.get(language);
        }


        /**
         * Get the description of this resource.
         *
         * @return a Map of descriptions keyed by language code
         */
        public Map<String, String> getDescription() {
            return description;
        }


        /**
         * Get the description of the resource for a specific language code.
         *
         * @param language
         *            the language code
         * @return the description for the language code or <code>null</code> if no
         *         title for this language code exists
         */
        public String getDescription(String language) {
            return (description != null) ? description.get(language) : null;
        }


        /**
         * Get the landing page of this resource.
         *
         * @return the landing page of this resource or <code>null</code> if not
         *         applicable
         */
        public String getLandingPageURI() {
            return landingPageURI;
        }


        /**
         * Get the list of languages in this resource represented as ISO-632-3 three
         * letter language code.
         *
         * @return the list of languages in this resource as a list of ISO-632-3
         *         three letter language codes.
         */
        public List<String> getLanguages() {
            return languages;
        }


        /**
         * Check, if this resource supports a certain language.
         *
         * @param language
         *            a language encoded as a ISO-632-3 three letter language
         *            code
         * @return <code>true</code> if the language is supported by this
         *         resource, <code>false</code> otherwise
         */
        public boolean supportsLanguage(String language) {
            if (language == null) {
                throw new NullPointerException("language == null");
            }
            for (String l : languages) {
                if (language.equals(l)) {
                    return true;
                }
            }
            return false;
        }


        /**
         * Get the direct sub-ordinate resources of this resource.
         *
         * @return a list of resources or <code>null</code> if this resource has
         *         no sub-ordinate resources
         */
        public List<ResourceInfo> getSubResources() {
            return subResources;
        }


        /**
         * Get the list of data views that are available for this resource.
         *
         * @return the list of data views available for this resource
         */
        public List<DataView> getAvailableDataViews() {
            return availableDataViews;
        }


        /**
         * (ADV-FCS) Get the list of layers that are available for this
         * resource.
         *
         * @return the list of layers views available for this resource
         */
        public List<Layer> getAvailableLayers() {
            return availableLayers;
        }
    } // class ResourceInfo

} // class ClarinFCSEndpointDescription

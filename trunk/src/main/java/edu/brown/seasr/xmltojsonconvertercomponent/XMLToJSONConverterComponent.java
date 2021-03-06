package edu.brown.seasr.xmltojsonconvertercomponent;

import edu.brown.seasr.AbstractXMLStreamComponent;
import edu.brown.seasr.QueueBlock;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.seasr.datatypes.core.Names;

import java.util.Queue;

/**
 * User: mdellabitta
 * Date: 2011-02-17
 * Time: 8:35 AM
 */
@Component(
        creator = "Michael Della Bitta",
        description = "This component converts JSON to XML.",
        name = "XML To JSON Converter",
        tags = "xml json converter",
        dependency = {"protobuf-java-2.2.0.jar", "json-lib-2.2.1-jdk15.jar", "saxon9he.jar"},
        baseURL = "meandre://brown.edu/seasr/tei/components/",
        firingPolicy = Component.FiringPolicy.any,
        mode = Component.Mode.compute,
        rights = Component.Licenses.Other)
public class XMLToJSONConverterComponent extends AbstractXMLStreamComponent {

    @ComponentOutput(description = "The combined XML document.", name = Names.PORT_JSON)
    final static String OUT_JSON = Names.PORT_JSON;

    @Override
    protected String[] getStreamOutputPorts() {
        return new String[] {OUT_JSON};
    }

    private XMLToJSONConverter converter = new XMLToJSONConverter();

    public void setConverter(XMLToJSONConverter converter) {
        this.converter = converter;
    }

    @Override
    protected boolean isSidechainReady() {
        return true;
    }

    @Override
    protected void processSidechainInputs(ComponentContext cc) throws Exception {
        //do nothing
    }

    @Override
    protected void processQueued(final ComponentContext cc, Queue<String> queue) {
        console.fine(String.format("Processing %d queued XML documents...", queue.size()));
        foreachDoc(new QueueBlock() {
            public void process(String xml) throws Exception {
                cc.pushDataComponentToOutput(OUT_JSON, converter.convert(xml));
            }
        });
    }
}

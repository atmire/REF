package org.dspace.ref.compliance.rules.factory;

import org.apache.commons.io.FileUtils;
import org.dspace.ref.compliance.definition.model.CategorySet;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategorySetMarshallerTest {

    private CategorySetMarshaller marshaller;

    @Before
    public void setUp() {
        marshaller = new CategorySetMarshaller();
    }

    @Test
    public void testUnmarshall() throws IOException, JAXBException {
        File file = FileUtils.toFile(CategorySetMarshaller.class.getResource("item-validation-rules.xml"));
        FileInputStream inputStream = new FileInputStream(file);

        CategorySet response = marshaller.unmarshal(inputStream);
        inputStream.close();

        assertNotNull(response);
        assertEquals(2, response.getCategory().size());
        assertEquals(2, response.getExceptions().getRule().size());
        assertEquals("notBlank", response.getCategory().get(0).getRules().getRule().get(0).getType());
        assertEquals("value", response.getCategory().get(0).getRules().getRule().get(1).getType());
        assertEquals("discoverable", response.getCategory().get(1).getRules().getRule().get(0).getType());
        assertEquals("countGreaterThan", response.getCategory().get(1).getRules().getRule().get(1).getType());
        assertEquals("dateRangeSmallerThan", response.getCategory().get(1).getRules().getRule().get(2).getType());
    }

}
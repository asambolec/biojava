/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.nbio.structure.io.mmcif.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "PdbxStructAssemblyGenXMLContainer")
public class PdbxStructAssemblyGenXMLContainer {

	private static final Logger logger = LoggerFactory.getLogger(PdbxStructAssemblyGenXMLContainer.class);

	static JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(PdbxStructAssemblyGenXMLContainer.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private List<PdbxStructAssemblyGen> data;

	@XmlElementWrapper
	public List<PdbxStructAssemblyGen> getPdbxStructAssemblyGens() {
		return data;

	}

	public void setPdbxStructAssemblies(List<PdbxStructAssemblyGen> d) {
		data = d;
	}

	public String toXML() {

		logger.info("converting to XML: " + data);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PrintStream ps = new PrintStream(baos);

		try {

			Marshaller m = jaxbContext.createMarshaller();

			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			m.marshal(this, ps);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return baos.toString();

	}

	public static PdbxStructAssemblyGenXMLContainer fromXML(String xml) {

		PdbxStructAssemblyGenXMLContainer job = null;

		try {

			Unmarshaller un = jaxbContext.createUnmarshaller();

			ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());

			job = (PdbxStructAssemblyGenXMLContainer) un.unmarshal(bais);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return job;
	}

}

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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.12.17 at 03:04:15 PM PST
//

package org.biojava.nbio.structure.validation;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.biojava.nbio.structure.validation
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: org.biojava.nbio.structure.validation
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link BondOutlier }
	 *
	 */
	public BondOutlier createBondOutlier() {
		return new BondOutlier();
	}

	/**
	 * Create an instance of {@link Programs }
	 *
	 */
	public Programs createPrograms() {
		return new Programs();
	}

	/**
	 * Create an instance of {@link Program }
	 *
	 */
	public Program createProgram() {
		return new Program();
	}

	/**
	 * Create an instance of {@link Entry }
	 *
	 */
	public Entry createEntry() {
		return new Entry();
	}

	/**
	 * Create an instance of {@link WwPDBValidationInformation }
	 *
	 */
	public WwPDBValidationInformation createWwPDBValidationInformation() {
		return new WwPDBValidationInformation();
	}

	/**
	 * Create an instance of {@link ModelledSubgroup }
	 *
	 */
	public ModelledSubgroup createModelledSubgroup() {
		return new ModelledSubgroup();
	}

	/**
	 * Create an instance of {@link AngleOutlier }
	 *
	 */
	public AngleOutlier createAngleOutlier() {
		return new AngleOutlier();
	}

	/**
	 * Create an instance of {@link Clash }
	 *
	 */
	public Clash createClash() {
		return new Clash();
	}

	/**
	 * Create an instance of {@link MogAngleOutlier }
	 *
	 */
	public MogAngleOutlier createMogAngleOutlier() {
		return new MogAngleOutlier();
	}

	/**
	 * Create an instance of {@link SymmClash }
	 *
	 */
	public SymmClash createSymmClash() {
		return new SymmClash();
	}

	/**
	 * Create an instance of {@link MogBondOutlier }
	 *
	 */
	public MogBondOutlier createMogBondOutlier() {
		return new MogBondOutlier();
	}

}

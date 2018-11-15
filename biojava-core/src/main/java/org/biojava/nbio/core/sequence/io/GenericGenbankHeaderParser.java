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
 * @author Karl Nicholas <github:karlnicholas>
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on 01-21-2010
 */
package org.biojava.nbio.core.sequence.io;

import org.biojava.nbio.core.exceptions.ParserException;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.io.template.SequenceHeaderParserInterface;
import org.biojava.nbio.core.sequence.reference.AbstractReference;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.core.sequence.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGenbankHeaderParser<S extends AbstractSequence<C>, C extends Compound>
		implements SequenceHeaderParserInterface<S, C> {

	private static final Logger logger = LoggerFactory.getLogger(GenericGenbankHeaderParser.class);
	private String accession = null;
	private String identifier = null;
	private String name = null;
	@SuppressWarnings("unused")
	private int version;
	private boolean versionSeen;
	private ArrayList<String> comments = new ArrayList<>();
	private List<AbstractReference> references = new ArrayList<>();
	private String description;

	/**
	 * Parse the header and set the values in the sequence
	 * 
	 * @param header
	 * @param sequence
	 */
	@Override
	public void parseHeader(String header, S sequence) {
		sequence.setOriginalHeader(header);
		sequence.setAccession(new AccessionID(accession, DataSource.GENBANK, version, identifier));
		sequence.setDescription(description);
		sequence.setComments(comments);
		sequence.setReferences(references);
	}

	/**
	 * Sets the sequence info back to default values, ie. in order to start
	 * constructing a new sequence from scratch.
	 */
	@SuppressWarnings("unused")
	private void reset() {
		this.version = 0;
		this.versionSeen = false;
		this.accession = null;
		this.description = null;
		this.identifier = null;
		this.name = null;
		this.comments.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVersion(int version) {
		if (this.versionSeen) {
			throw new ParserException("Current BioEntry already has a version");
		} else {
			try {
				this.version = version;
				this.versionSeen = true;
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				throw new ParserException("Could not parse version as an integer");
			}
		}
	}

	/**
	 * {@inheritDoc} The last accession passed to this routine will always be the
	 * one used.
	 */
	public void setAccession(String accession) {
		if (accession == null) {
			throw new ParserException("Accession cannot be null");
		}
		this.accession = accession;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDescription(String description) {
		if (this.description != null) {
			throw new ParserException("Current BioEntry already has a description");
		}
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIdentifier(String identifier) {
		if (identifier == null) {
			throw new ParserException("Identifier cannot be null");
		}
		if (this.identifier != null) {
			throw new ParserException("Current BioEntry already has a identifier");
		}
		this.identifier = identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) {
		if (name == null) {
			throw new ParserException("Name cannot be null");
		}
		if (this.name != null) {
			throw new ParserException("Current BioEntry already has a name");
		}
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setComment(String comment) {
		if (comment == null) {
			throw new ParserException("Comment cannot be null");
		}
		this.comments.add(comment);
	}

	public void addReference(AbstractReference abstractReference) {
		this.references.add(abstractReference);
	}
}

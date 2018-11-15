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

/**
 * Bean to hold data for _pdbx_audit_revision_history mmCIF category.
 * 
 * @author Peter Rose
 * @since 5.0
 */
public class PdbxAuditRevisionHistory extends AbstractBean {
	private String ordinal;
	private String dataContentType;
	private String majorRevision;
	private String minorRevision;
	private String revisionDate;

	public String getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(String ordinal) {
		this.ordinal = ordinal;
	}

	public String getData_content_type() {
		return dataContentType;
	}

	public void setData_content_type(String data_content_type) {
		this.dataContentType = data_content_type;
	}

	public String getMajor_revision() {
		return majorRevision;
	}

	public void setMajor_revision(String major_revision) {
		this.majorRevision = major_revision;
	}

	public String getMinor_revision() {
		return minorRevision;
	}

	public void setMinor_revision(String minor_revision) {
		this.minorRevision = minor_revision;
	}

	public String getRevision_date() {
		return revisionDate;
	}

	public void setRevision_date(String revision_date) {
		this.revisionDate = revision_date;
	}
}

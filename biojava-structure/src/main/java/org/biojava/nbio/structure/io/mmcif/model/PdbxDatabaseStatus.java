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
 * Bean to hold data for _pdbx_database_status mmCIF category.
 * 
 * @author Peter Rose
 * @since 5.0
 */
public class PdbxDatabaseStatus extends AbstractBean {
	private String statusCode;
	private String entryId;
	private String recvdInitialDepositionDate;
	private String depositSite;
	private String processSite;
	private String sgEntry;
	private String pdbFormatCompatible;
	private String statusCodeMr;
	private String statusCodeSf;
	private String statusCodeCs;

	public String getStatus_code() {
		return statusCode;
	}

	public void setStatus_code(String status_code) {
		this.statusCode = status_code;
	}

	public String getEntry_id() {
		return entryId;
	}

	public void setEntry_id(String entry_id) {
		this.entryId = entry_id;
	}

	public String getRecvd_initial_deposition_date() {
		return recvdInitialDepositionDate;
	}

	public void setRecvd_initial_deposition_date(String recvd_initial_deposition_date) {
		this.recvdInitialDepositionDate = recvd_initial_deposition_date;
	}

	public String getDeposit_site() {
		return depositSite;
	}

	public void setDeposit_site(String deposit_site) {
		this.depositSite = deposit_site;
	}

	public String getProcess_site() {
		return processSite;
	}

	public void setProcess_site(String process_site) {
		this.processSite = process_site;
	}

	public String getSG_entry() {
		return sgEntry;
	}

	public void setSG_entry(String sG_entry) {
		sgEntry = sG_entry;
	}

	public String getPdb_format_compatible() {
		return pdbFormatCompatible;
	}

	public void setPdb_format_compatible(String pdb_format_compatible) {
		this.pdbFormatCompatible = pdb_format_compatible;
	}

	public String getStatus_code_mr() {
		return statusCodeMr;
	}

	public void setStatus_code_mr(String status_code_mr) {
		this.statusCodeMr = status_code_mr;
	}

	public String getStatus_code_sf() {
		return statusCodeSf;
	}

	public void setStatus_code_sf(String status_code_sf) {
		this.statusCodeSf = status_code_sf;
	}

	public String getStatus_code_cs() {
		return statusCodeCs;
	}

	public void setStatus_code_cs(String status_code_cs) {
		this.statusCodeCs = status_code_cs;
	}
}

/*
 *                    PDB web development code
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
 *
 * Created on Mar 05, 2014
 * Created by Peter Rose
 *
 */

package org.biojava.nbio.structure.io.mmcif.model;

/**
 * A bean that stores data from the mmcif category _struct_conn
 * 
 * @author Peter Rose
 *
 */
public class StructConn extends AbstractBean {
	private String id;
	private String connTypeId;
	private String pdbxPdbId;
	private String ptnr1LabelAsymId;
	private String ptnr1LabelCompId;
	private String ptnr1LabelSeqId;
	private String ptnr1LabelAtomId;
	private String pdbxPtnr1LabelAltId;
	private String pdbxPtnr1PdbInsCode;
	private String pdbxPtnr1StandardCompId;
	private String ptnr1Symmetry;
	private String ptnr2LabelAsymId;
	private String ptnr2LabelCompId;
	private String ptnr2LabelSeqId;
	private String ptnr2LabelAtomId;
	private String pdbxPtnr2LabelAltId;
	private String pdbxPtnr2PdbInsCode;
	private String ptnr1AuthAsymId;
	private String ptnr1AuthCompId;
	private String ptnr1AuthSeqId;
	private String ptnr2AuthAsymId;
	private String ptnr2AuthCompId;
	private String ptnr2AuthSeqId;
	private String ptnr2Symmetry;
	private String pdbxPtnr3LabelAtomId;
	private String pdbxPtnr3LabelSeqId;
	private String pdbxPtnr3LabelCompId;
	private String pdbxPtnr3LabelAsymId;
	private String pdbxPtnr3LabelAltId;
	private String pdbxPtnr3PdbInsCode;
	private String details;
	private String pdbxDistValue;
	private String pdbxValueOrder;
	private String pdbxLeavingAtomFlag;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the conn_type_id
	 */
	public String getConn_type_id() {
		return connTypeId;
	}

	/**
	 * @param conn_type_id the conn_type_id to set
	 */
	public void setConn_type_id(String conn_type_id) {
		this.connTypeId = conn_type_id;
	}

	/**
	 * @return the pdbx_PDB_id
	 */
	public String getPdbx_PDB_id() {
		return pdbxPdbId;
	}

	/**
	 * @param pdbx_PDB_id the pdbx_PDB_id to set
	 */
	public void setPdbx_PDB_id(String pdbx_PDB_id) {
		this.pdbxPdbId = pdbx_PDB_id;
	}

	/**
	 * @return the ptnr1_label_asym_id
	 */
	public String getPtnr1_label_asym_id() {
		return ptnr1LabelAsymId;
	}

	/**
	 * @param ptnr1_label_asym_id the ptnr1_label_asym_id to set
	 */
	public void setPtnr1_label_asym_id(String ptnr1_label_asym_id) {
		this.ptnr1LabelAsymId = ptnr1_label_asym_id;
	}

	/**
	 * @return the ptnr1_label_comp_id
	 */
	public String getPtnr1_label_comp_id() {
		return ptnr1LabelCompId;
	}

	/**
	 * @param ptnr1_label_comp_id the ptnr1_label_comp_id to set
	 */
	public void setPtnr1_label_comp_id(String ptnr1_label_comp_id) {
		this.ptnr1LabelCompId = ptnr1_label_comp_id;
	}

	/**
	 * @return the ptnr1_label_seq_id
	 */
	public String getPtnr1_label_seq_id() {
		return ptnr1LabelSeqId;
	}

	/**
	 * @param ptnr1_label_seq_id the ptnr1_label_seq_id to set
	 */
	public void setPtnr1_label_seq_id(String ptnr1_label_seq_id) {
		this.ptnr1LabelSeqId = ptnr1_label_seq_id;
	}

	/**
	 * @return the ptnr1_label_atom_id
	 */
	public String getPtnr1_label_atom_id() {
		return ptnr1LabelAtomId;
	}

	/**
	 * @param ptnr1_label_atom_id the ptnr1_label_atom_id to set
	 */
	public void setPtnr1_label_atom_id(String ptnr1_label_atom_id) {
		this.ptnr1LabelAtomId = ptnr1_label_atom_id;
	}

	/**
	 * @return the pdbx_ptnr1_label_alt_id
	 */
	public String getPdbx_ptnr1_label_alt_id() {
		return pdbxPtnr1LabelAltId;
	}

	/**
	 * @param pdbx_ptnr1_label_alt_id the pdbx_ptnr1_label_alt_id to set
	 */
	public void setPdbx_ptnr1_label_alt_id(String pdbx_ptnr1_label_alt_id) {
		this.pdbxPtnr1LabelAltId = pdbx_ptnr1_label_alt_id;
	}

	/**
	 * @return the pdbx_ptnr1_PDB_ins_code
	 */
	public String getPdbx_ptnr1_PDB_ins_code() {
		return pdbxPtnr1PdbInsCode;
	}

	/**
	 * @param pdbx_ptnr1_PDB_ins_code the pdbx_ptnr1_PDB_ins_code to set
	 */
	public void setPdbx_ptnr1_PDB_ins_code(String pdbx_ptnr1_PDB_ins_code) {
		this.pdbxPtnr1PdbInsCode = pdbx_ptnr1_PDB_ins_code;
	}

	/**
	 * @return the pdbx_ptnr1_standard_comp_id
	 */
	public String getPdbx_ptnr1_standard_comp_id() {
		return pdbxPtnr1StandardCompId;
	}

	/**
	 * @param pdbx_ptnr1_standard_comp_id the pdbx_ptnr1_standard_comp_id to set
	 */
	public void setPdbx_ptnr1_standard_comp_id(String pdbx_ptnr1_standard_comp_id) {
		this.pdbxPtnr1StandardCompId = pdbx_ptnr1_standard_comp_id;
	}

	/**
	 * @return the ptnr1_symmetry
	 */
	public String getPtnr1_symmetry() {
		return ptnr1Symmetry;
	}

	/**
	 * @param ptnr1_symmetry the ptnr1_symmetry to set
	 */
	public void setPtnr1_symmetry(String ptnr1_symmetry) {
		this.ptnr1Symmetry = ptnr1_symmetry;
	}

	/**
	 * @return the ptnr2_label_asym_id
	 */
	public String getPtnr2_label_asym_id() {
		return ptnr2LabelAsymId;
	}

	/**
	 * @param ptnr2_label_asym_id the ptnr2_label_asym_id to set
	 */
	public void setPtnr2_label_asym_id(String ptnr2_label_asym_id) {
		this.ptnr2LabelAsymId = ptnr2_label_asym_id;
	}

	/**
	 * @return the ptnr2_label_comp_id
	 */
	public String getPtnr2_label_comp_id() {
		return ptnr2LabelCompId;
	}

	/**
	 * @param ptnr2_label_comp_id the ptnr2_label_comp_id to set
	 */
	public void setPtnr2_label_comp_id(String ptnr2_label_comp_id) {
		this.ptnr2LabelCompId = ptnr2_label_comp_id;
	}

	/**
	 * @return the ptnr2_label_seq_id
	 */
	public String getPtnr2_label_seq_id() {
		return ptnr2LabelSeqId;
	}

	/**
	 * @param ptnr2_label_seq_id the ptnr2_label_seq_id to set
	 */
	public void setPtnr2_label_seq_id(String ptnr2_label_seq_id) {
		this.ptnr2LabelSeqId = ptnr2_label_seq_id;
	}

	/**
	 * @return the ptnr2_label_atom_id
	 */
	public String getPtnr2_label_atom_id() {
		return ptnr2LabelAtomId;
	}

	/**
	 * @param ptnr2_label_atom_id the ptnr2_label_atom_id to set
	 */
	public void setPtnr2_label_atom_id(String ptnr2_label_atom_id) {
		this.ptnr2LabelAtomId = ptnr2_label_atom_id;
	}

	/**
	 * @return the pdbx_ptnr2_label_alt_id
	 */
	public String getPdbx_ptnr2_label_alt_id() {
		return pdbxPtnr2LabelAltId;
	}

	/**
	 * @param pdbx_ptnr2_label_alt_id the pdbx_ptnr2_label_alt_id to set
	 */
	public void setPdbx_ptnr2_label_alt_id(String pdbx_ptnr2_label_alt_id) {
		this.pdbxPtnr2LabelAltId = pdbx_ptnr2_label_alt_id;
	}

	/**
	 * @return the pdbx_ptnr2_PDB_ins_code
	 */
	public String getPdbx_ptnr2_PDB_ins_code() {
		return pdbxPtnr2PdbInsCode;
	}

	/**
	 * @param pdbx_ptnr2_PDB_ins_code the pdbx_ptnr2_PDB_ins_code to set
	 */
	public void setPdbx_ptnr2_PDB_ins_code(String pdbx_ptnr2_PDB_ins_code) {
		this.pdbxPtnr2PdbInsCode = pdbx_ptnr2_PDB_ins_code;
	}

	/**
	 * @return the ptnr1_auth_asym_id
	 */
	public String getPtnr1_auth_asym_id() {
		return ptnr1AuthAsymId;
	}

	/**
	 * @param ptnr1_auth_asym_id the ptnr1_auth_asym_id to set
	 */
	public void setPtnr1_auth_asym_id(String ptnr1_auth_asym_id) {
		this.ptnr1AuthAsymId = ptnr1_auth_asym_id;
	}

	/**
	 * @return the ptnr1_auth_comp_id
	 */
	public String getPtnr1_auth_comp_id() {
		return ptnr1AuthCompId;
	}

	/**
	 * @param ptnr1_auth_comp_id the ptnr1_auth_comp_id to set
	 */
	public void setPtnr1_auth_comp_id(String ptnr1_auth_comp_id) {
		this.ptnr1AuthCompId = ptnr1_auth_comp_id;
	}

	/**
	 * @return the ptnr1_auth_seq_id
	 */
	public String getPtnr1_auth_seq_id() {
		return ptnr1AuthSeqId;
	}

	/**
	 * @param ptnr1_auth_seq_id the ptnr1_auth_seq_id to set
	 */
	public void setPtnr1_auth_seq_id(String ptnr1_auth_seq_id) {
		this.ptnr1AuthSeqId = ptnr1_auth_seq_id;
	}

	/**
	 * @return the ptnr2_auth_asym_id
	 */
	public String getPtnr2_auth_asym_id() {
		return ptnr2AuthAsymId;
	}

	/**
	 * @param ptnr2_auth_asym_id the ptnr2_auth_asym_id to set
	 */
	public void setPtnr2_auth_asym_id(String ptnr2_auth_asym_id) {
		this.ptnr2AuthAsymId = ptnr2_auth_asym_id;
	}

	/**
	 * @return the ptnr2_auth_comp_id
	 */
	public String getPtnr2_auth_comp_id() {
		return ptnr2AuthCompId;
	}

	/**
	 * @param ptnr2_auth_comp_id the ptnr2_auth_comp_id to set
	 */
	public void setPtnr2_auth_comp_id(String ptnr2_auth_comp_id) {
		this.ptnr2AuthCompId = ptnr2_auth_comp_id;
	}

	/**
	 * @return the ptnr2_auth_seq_id
	 */
	public String getPtnr2_auth_seq_id() {
		return ptnr2AuthSeqId;
	}

	/**
	 * @param ptnr2_auth_seq_id the ptnr2_auth_seq_id to set
	 */
	public void setPtnr2_auth_seq_id(String ptnr2_auth_seq_id) {
		this.ptnr2AuthSeqId = ptnr2_auth_seq_id;
	}

	/**
	 * @return the ptnr2_symmetry
	 */
	public String getPtnr2_symmetry() {
		return ptnr2Symmetry;
	}

	/**
	 * @param ptnr2_symmetry the ptnr2_symmetry to set
	 */
	public void setPtnr2_symmetry(String ptnr2_symmetry) {
		this.ptnr2Symmetry = ptnr2_symmetry;
	}

	/**
	 * @return the pdbx_ptnr3_label_atom_id
	 */
	public String getPdbx_ptnr3_label_atom_id() {
		return pdbxPtnr3LabelAtomId;
	}

	/**
	 * @param pdbx_ptnr3_label_atom_id the pdbx_ptnr3_label_atom_id to set
	 */
	public void setPdbx_ptnr3_label_atom_id(String pdbx_ptnr3_label_atom_id) {
		this.pdbxPtnr3LabelAtomId = pdbx_ptnr3_label_atom_id;
	}

	/**
	 * @return the pdbx_ptnr3_label_seq_id
	 */
	public String getPdbx_ptnr3_label_seq_id() {
		return pdbxPtnr3LabelSeqId;
	}

	/**
	 * @param pdbx_ptnr3_label_seq_id the pdbx_ptnr3_label_seq_id to set
	 */
	public void setPdbx_ptnr3_label_seq_id(String pdbx_ptnr3_label_seq_id) {
		this.pdbxPtnr3LabelSeqId = pdbx_ptnr3_label_seq_id;
	}

	/**
	 * @return the pdbx_ptnr3_label_comp_id
	 */
	public String getPdbx_ptnr3_label_comp_id() {
		return pdbxPtnr3LabelCompId;
	}

	/**
	 * @param pdbx_ptnr3_label_comp_id the pdbx_ptnr3_label_comp_id to set
	 */
	public void setPdbx_ptnr3_label_comp_id(String pdbx_ptnr3_label_comp_id) {
		this.pdbxPtnr3LabelCompId = pdbx_ptnr3_label_comp_id;
	}

	/**
	 * @return the pdbx_ptnr3_label_asym_id
	 */
	public String getPdbx_ptnr3_label_asym_id() {
		return pdbxPtnr3LabelAsymId;
	}

	/**
	 * @param pdbx_ptnr3_label_asym_id the pdbx_ptnr3_label_asym_id to set
	 */
	public void setPdbx_ptnr3_label_asym_id(String pdbx_ptnr3_label_asym_id) {
		this.pdbxPtnr3LabelAsymId = pdbx_ptnr3_label_asym_id;
	}

	/**
	 * @return the pdbx_ptnr3_label_alt_id
	 */
	public String getPdbx_ptnr3_label_alt_id() {
		return pdbxPtnr3LabelAltId;
	}

	/**
	 * @param pdbx_ptnr3_label_alt_id the pdbx_ptnr3_label_alt_id to set
	 */
	public void setPdbx_ptnr3_label_alt_id(String pdbx_ptnr3_label_alt_id) {
		this.pdbxPtnr3LabelAltId = pdbx_ptnr3_label_alt_id;
	}

	/**
	 * @return the pdbx_ptnr3_PDB_ins_code
	 */
	public String getPdbx_ptnr3_PDB_ins_code() {
		return pdbxPtnr3PdbInsCode;
	}

	/**
	 * @param pdbx_ptnr3_PDB_ins_code the pdbx_ptnr3_PDB_ins_code to set
	 */
	public void setPdbx_ptnr3_PDB_ins_code(String pdbx_ptnr3_PDB_ins_code) {
		this.pdbxPtnr3PdbInsCode = pdbx_ptnr3_PDB_ins_code;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the pdbx_dist_value
	 */
	public String getPdbx_dist_value() {
		return pdbxDistValue;
	}

	/**
	 * @param pdbx_dist_value the pdbx_dist_value to set
	 */
	public void setPdbx_dist_value(String pdbx_dist_value) {
		this.pdbxDistValue = pdbx_dist_value;
	}

	/**
	 * @return the pdbx_value_order
	 */
	public String getPdbx_value_order() {
		return pdbxValueOrder;
	}

	/**
	 * @param pdbx_value_order the pdbx_value_order to set
	 */
	public void setPdbx_value_order(String pdbx_value_order) {
		this.pdbxValueOrder = pdbx_value_order;
	}

	public String getPdbx_leaving_atom_flag() {
		return pdbxLeavingAtomFlag;
	}

	public void setPdbx_leaving_atom_flag(String pdbx_leaving_atom_flag) {
		this.pdbxLeavingAtomFlag = pdbx_leaving_atom_flag;
	}
}

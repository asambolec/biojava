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

import org.biojava.nbio.structure.io.mmcif.chem.ChemCompTools;
import org.biojava.nbio.structure.io.mmcif.chem.PolymerType;
import org.biojava.nbio.structure.io.mmcif.chem.ResidueType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A definition for a Chemical Component, as maintained by the wwPDB. For access
 * to all definitions, please download the components.cif.gz file from the wwPDB
 * website.
 *
 * @author Andreas Prlic
 *
 */
public class ChemComp implements Serializable, Comparable<ChemComp> {
	/**
	 *
	 */
	private static final long serialVersionUID = -4736341142030215915L;

	private String id;
	private String name;
	private String type;
	private String pdbxType;
	private String formula;
	private String monNstdParentCompId;
	private String pdbxSynonyms;
	private String pdbxFormalCharge;
	private String pdbxInitialDate;
	private String pdbxModifiedDate;
	private String pdbxAmbiguousFlag;
	private String pdbxReleaseStatus;
	private String pdbxReplacedBy;
	private String pdbxReplaces;
	private String formulaWeight;
	private String oneLetterCode;
	private String threeLetterCode;
	private String pdbxModelCoordinatesDetails;
	private String pdbxModelCoordinatesMissingFlag;
	private String pdbxIdealCoordinatesDetails;
	private String pdbxIdealCoordinatesMissingFlag;
	private String pdbxModelCoordinatesDbCode;
	private String pdbxSubcomponentList;
	private String pdbxProcessingSite;
	private String monNstdFlag;

	@IgnoreField
	private List<ChemCompDescriptor> descriptors = new ArrayList<>();
	@IgnoreField
	private List<ChemCompBond> bonds = new ArrayList<>();
	@IgnoreField
	private List<ChemCompAtom> atoms = new ArrayList<>();

	// and some derived data for easier processing...
	@IgnoreField
	private ResidueType residueType;
	@IgnoreField
	private PolymerType polymerType;
	@IgnoreField
	private boolean standard;

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("ChemComp ");
		buf.append(id);
		buf.append(" ");
		buf.append(oneLetterCode);
		buf.append(" ");
		buf.append(threeLetterCode);
		buf.append(" poly:");
		buf.append(getPolymerType());
		buf.append(" resi:");
		buf.append(getResidueType());
		if (isStandard()) {
			buf.append(" standard");
		} else {
			buf.append(" modified");
		}
		buf.append(" ");

		buf.append(name);
		buf.append(" ");
		buf.append(pdbxType);
		buf.append(" ");
		buf.append(formula);
		buf.append(" parent:");
		buf.append(monNstdParentCompId);
		return buf.toString();
	}

	public boolean hasParent() {
		String pid = monNstdParentCompId;
		if ((pid != null) && (!"?".equals(pid))) {
			return true;
		}
		return false;
	}

	public boolean isStandard() {
		return standard;
	}

	private void setStandardFlag() {
		standard = ChemCompTools.isStandardChemComp(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;

		residueType = ResidueType.getResidueTypeFromString(type);
		if (residueType != null) {
			polymerType = residueType.polymerType;
		}

	}

	public ResidueType getResidueType() {
		return residueType;
	}

	public void setResidueType(ResidueType residueType) {
		this.residueType = residueType;
	}

	public PolymerType getPolymerType() {
		return polymerType;
	}

	public void setPolymerType(PolymerType polymerType) {
		this.polymerType = polymerType;
	}

	public String getPdbx_type() {
		return pdbxType;
	}

	public void setPdbx_type(String pdbx_type) {
		this.pdbxType = pdbx_type;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getMon_nstd_parent_comp_id() {
		return monNstdParentCompId;
	}

	public void setMon_nstd_parent_comp_id(String mon_nstd_parent_comp_id) {
		this.monNstdParentCompId = mon_nstd_parent_comp_id;
		setStandardFlag();
	}

	public String getPdbx_synonyms() {
		return pdbxSynonyms;
	}

	public void setPdbx_synonyms(String pdbx_synonyms) {
		this.pdbxSynonyms = pdbx_synonyms;
	}

	public String getPdbx_formal_charge() {
		return pdbxFormalCharge;
	}

	public void setPdbx_formal_charge(String pdbx_formal_charge) {
		this.pdbxFormalCharge = pdbx_formal_charge;
	}

	public String getPdbx_initial_date() {
		return pdbxInitialDate;
	}

	public void setPdbx_initial_date(String pdbx_initial_date) {
		this.pdbxInitialDate = pdbx_initial_date;
	}

	public String getPdbx_modified_date() {
		return pdbxModifiedDate;
	}

	public void setPdbx_modified_date(String pdbx_modified_date) {
		this.pdbxModifiedDate = pdbx_modified_date;
	}

	public String getPdbx_ambiguous_flag() {
		return pdbxAmbiguousFlag;
	}

	public void setPdbx_ambiguous_flag(String pdbx_ambiguous_flag) {
		this.pdbxAmbiguousFlag = pdbx_ambiguous_flag;
	}

	public String getPdbx_release_status() {
		return pdbxReleaseStatus;
	}

	public void setPdbx_release_status(String pdbx_release_status) {
		this.pdbxReleaseStatus = pdbx_release_status;
	}

	public String getPdbx_replaced_by() {
		return pdbxReplacedBy;
	}

	public void setPdbx_replaced_by(String pdbx_replaced_by) {
		this.pdbxReplacedBy = pdbx_replaced_by;
	}

	public String getPdbx_replaces() {
		return pdbxReplaces;
	}

	public void setPdbx_replaces(String pdbx_replaces) {
		this.pdbxReplaces = pdbx_replaces;
	}

	public String getFormula_weight() {
		return formulaWeight;
	}

	public void setFormula_weight(String formula_weight) {
		this.formulaWeight = formula_weight;
	}

	public String getOne_letter_code() {
		return oneLetterCode;
	}

	public void setOne_letter_code(String one_letter_code) {
		this.oneLetterCode = one_letter_code;
		setStandardFlag();
	}

	public String getThree_letter_code() {
		return threeLetterCode;
	}

	public void setThree_letter_code(String three_letter_code) {
		this.threeLetterCode = three_letter_code;
	}

	public String getPdbx_model_coordinates_details() {
		return pdbxModelCoordinatesDetails;
	}

	public void setPdbx_model_coordinates_details(String pdbx_model_coordinates_details) {
		this.pdbxModelCoordinatesDetails = pdbx_model_coordinates_details;
	}

	public String getPdbx_model_coordinates_missing_flag() {
		return pdbxModelCoordinatesMissingFlag;
	}

	public void setPdbx_model_coordinates_missing_flag(String pdbx_model_coordinates_missing_flag) {
		this.pdbxModelCoordinatesMissingFlag = pdbx_model_coordinates_missing_flag;
	}

	public String getPdbx_ideal_coordinates_details() {
		return pdbxIdealCoordinatesDetails;
	}

	public void setPdbx_ideal_coordinates_details(String pdbx_ideal_coordinates_details) {
		this.pdbxIdealCoordinatesDetails = pdbx_ideal_coordinates_details;
	}

	public String getPdbx_ideal_coordinates_missing_flag() {
		return pdbxIdealCoordinatesMissingFlag;
	}

	public void setPdbx_ideal_coordinates_missing_flag(String pdbx_ideal_coordinates_missing_flag) {
		this.pdbxIdealCoordinatesMissingFlag = pdbx_ideal_coordinates_missing_flag;
	}

	public String getPdbx_model_coordinates_db_code() {
		return pdbxModelCoordinatesDbCode;
	}

	public void setPdbx_model_coordinates_db_code(String pdbx_model_coordinates_db_code) {
		this.pdbxModelCoordinatesDbCode = pdbx_model_coordinates_db_code;
	}

	public String getPdbx_subcomponent_list() {
		return pdbxSubcomponentList;
	}

	public void setPdbx_subcomponent_list(String pdbx_subcomponent_list) {
		this.pdbxSubcomponentList = pdbx_subcomponent_list;
	}

	public String getPdbx_processing_site() {
		return pdbxProcessingSite;
	}

	public void setPdbx_processing_site(String pdbx_processing_site) {
		this.pdbxProcessingSite = pdbx_processing_site;
	}

	public void setStandard(boolean standard) {
		this.standard = standard;
	}

	public String getMon_nstd_flag() {
		return monNstdFlag;
	}

	public void setMon_nstd_flag(String mon_nstd_flag) {
		this.monNstdFlag = mon_nstd_flag;
	}

	public List<ChemCompDescriptor> getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(List<ChemCompDescriptor> descriptors) {
		this.descriptors = descriptors;
	}

	public List<ChemCompBond> getBonds() {
		return bonds;
	}

	public void setBonds(List<ChemCompBond> bonds) {
		this.bonds = bonds;
	}

	public List<ChemCompAtom> getAtoms() {
		return atoms;
	}

	public void setAtoms(List<ChemCompAtom> atoms) {
		this.atoms = atoms;
	}

	@Override
	public int compareTo(ChemComp arg0) {
		if (this.equals(arg0)) {
			return 0;
		}
		return this.getId().compareTo(arg0.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descriptors == null) ? 0 : descriptors.hashCode());
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result + ((formulaWeight == null) ? 0 : formulaWeight.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((monNstdFlag == null) ? 0 : monNstdFlag.hashCode());
		result = prime * result + ((monNstdParentCompId == null) ? 0 : monNstdParentCompId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((oneLetterCode == null) ? 0 : oneLetterCode.hashCode());
		result = prime * result + ((pdbxAmbiguousFlag == null) ? 0 : pdbxAmbiguousFlag.hashCode());
		result = prime * result + ((pdbxFormalCharge == null) ? 0 : pdbxFormalCharge.hashCode());
		result = prime * result + ((pdbxIdealCoordinatesDetails == null) ? 0 : pdbxIdealCoordinatesDetails.hashCode());
		result = prime * result
				+ ((pdbxIdealCoordinatesMissingFlag == null) ? 0 : pdbxIdealCoordinatesMissingFlag.hashCode());
		result = prime * result + ((pdbxInitialDate == null) ? 0 : pdbxInitialDate.hashCode());
		result = prime * result + ((pdbxModelCoordinatesDbCode == null) ? 0 : pdbxModelCoordinatesDbCode.hashCode());
		result = prime * result + ((pdbxModelCoordinatesDetails == null) ? 0 : pdbxModelCoordinatesDetails.hashCode());
		result = prime * result
				+ ((pdbxModelCoordinatesMissingFlag == null) ? 0 : pdbxModelCoordinatesMissingFlag.hashCode());
		result = prime * result + ((pdbxModifiedDate == null) ? 0 : pdbxModifiedDate.hashCode());
		result = prime * result + ((pdbxProcessingSite == null) ? 0 : pdbxProcessingSite.hashCode());
		result = prime * result + ((pdbxReleaseStatus == null) ? 0 : pdbxReleaseStatus.hashCode());
		result = prime * result + ((pdbxReplacedBy == null) ? 0 : pdbxReplacedBy.hashCode());
		result = prime * result + ((pdbxReplaces == null) ? 0 : pdbxReplaces.hashCode());
		result = prime * result + ((pdbxSubcomponentList == null) ? 0 : pdbxSubcomponentList.hashCode());
		result = prime * result + ((pdbxSynonyms == null) ? 0 : pdbxSynonyms.hashCode());
		result = prime * result + ((pdbxType == null) ? 0 : pdbxType.hashCode());
		result = prime * result + ((polymerType == null) ? 0 : polymerType.hashCode());
		result = prime * result + ((residueType == null) ? 0 : residueType.hashCode());
		result = prime * result + (standard ? 1231 : 1237);
		result = prime * result + ((threeLetterCode == null) ? 0 : threeLetterCode.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ChemComp other = (ChemComp) obj;
		if (descriptors == null) {
			if (other.descriptors != null) {
				return false;
			}
		} else if (!descriptors.equals(other.descriptors)) {
			return false;
		}
		if (formula == null) {
			if (other.formula != null) {
				return false;
			}
		} else if (!formula.equals(other.formula)) {
			return false;
		}
		if (formulaWeight == null) {
			if (other.formulaWeight != null) {
				return false;
			}
		} else if (!formulaWeight.equals(other.formulaWeight)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (monNstdFlag == null) {
			if (other.monNstdFlag != null) {
				return false;
			}
		} else if (!monNstdFlag.equals(other.monNstdFlag)) {
			return false;
		}
		if (monNstdParentCompId == null) {
			if (other.monNstdParentCompId != null) {
				return false;
			}
		} else if (!monNstdParentCompId.equals(other.monNstdParentCompId)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (oneLetterCode == null) {
			if (other.oneLetterCode != null) {
				return false;
			}
		} else if (!oneLetterCode.equals(other.oneLetterCode)) {
			return false;
		}
		if (pdbxAmbiguousFlag == null) {
			if (other.pdbxAmbiguousFlag != null) {
				return false;
			}
		} else if (!pdbxAmbiguousFlag.equals(other.pdbxAmbiguousFlag)) {
			return false;
		}
		if (pdbxFormalCharge == null) {
			if (other.pdbxFormalCharge != null) {
				return false;
			}
		} else if (!pdbxFormalCharge.equals(other.pdbxFormalCharge)) {
			return false;
		}
		if (pdbxIdealCoordinatesDetails == null) {
			if (other.pdbxIdealCoordinatesDetails != null) {
				return false;
			}
		} else if (!pdbxIdealCoordinatesDetails.equals(other.pdbxIdealCoordinatesDetails)) {
			return false;
		}
		if (pdbxIdealCoordinatesMissingFlag == null) {
			if (other.pdbxIdealCoordinatesMissingFlag != null) {
				return false;
			}
		} else if (!pdbxIdealCoordinatesMissingFlag.equals(other.pdbxIdealCoordinatesMissingFlag)) {
			return false;
		}
		if (pdbxInitialDate == null) {
			if (other.pdbxInitialDate != null) {
				return false;
			}
		} else if (!pdbxInitialDate.equals(other.pdbxInitialDate)) {
			return false;
		}
		if (pdbxModelCoordinatesDbCode == null) {
			if (other.pdbxModelCoordinatesDbCode != null) {
				return false;
			}
		} else if (!pdbxModelCoordinatesDbCode.equals(other.pdbxModelCoordinatesDbCode)) {
			return false;
		}
		if (pdbxModelCoordinatesDetails == null) {
			if (other.pdbxModelCoordinatesDetails != null) {
				return false;
			}
		} else if (!pdbxModelCoordinatesDetails.equals(other.pdbxModelCoordinatesDetails)) {
			return false;
		}
		if (pdbxModelCoordinatesMissingFlag == null) {
			if (other.pdbxModelCoordinatesMissingFlag != null) {
				return false;
			}
		} else if (!pdbxModelCoordinatesMissingFlag.equals(other.pdbxModelCoordinatesMissingFlag)) {
			return false;
		}
		if (pdbxModifiedDate == null) {
			if (other.pdbxModifiedDate != null) {
				return false;
			}
		} else if (!pdbxModifiedDate.equals(other.pdbxModifiedDate)) {
			return false;
		}
		if (pdbxProcessingSite == null) {
			if (other.pdbxProcessingSite != null) {
				return false;
			}
		} else if (!pdbxProcessingSite.equals(other.pdbxProcessingSite)) {
			return false;
		}
		if (pdbxReleaseStatus == null) {
			if (other.pdbxReleaseStatus != null) {
				return false;
			}
		} else if (!pdbxReleaseStatus.equals(other.pdbxReleaseStatus)) {
			return false;
		}
		if (pdbxReplacedBy == null) {
			if (other.pdbxReplacedBy != null) {
				return false;
			}
		} else if (!pdbxReplacedBy.equals(other.pdbxReplacedBy)) {
			return false;
		}
		if (pdbxReplaces == null) {
			if (other.pdbxReplaces != null) {
				return false;
			}
		} else if (!pdbxReplaces.equals(other.pdbxReplaces)) {
			return false;
		}
		if (pdbxSubcomponentList == null) {
			if (other.pdbxSubcomponentList != null) {
				return false;
			}
		} else if (!pdbxSubcomponentList.equals(other.pdbxSubcomponentList)) {
			return false;
		}
		if (pdbxSynonyms == null) {
			if (other.pdbxSynonyms != null) {
				return false;
			}
		} else if (!pdbxSynonyms.equals(other.pdbxSynonyms)) {
			return false;
		}
		if (pdbxType == null) {
			if (other.pdbxType != null) {
				return false;
			}
		} else if (!pdbxType.equals(other.pdbxType)) {
			return false;
		}
		if (polymerType != other.polymerType) {
			return false;
		}
		if (residueType != other.residueType) {
			return false;
		}
		if (standard != other.standard) {
			return false;
		}
		if (threeLetterCode == null) {
			if (other.threeLetterCode != null) {
				return false;
			}
		} else if (!threeLetterCode.equals(other.threeLetterCode)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	/**
	 * Creates a new instance of the dummy empty ChemComp.
	 * 
	 * @return
	 */
	public static ChemComp getEmptyChemComp() {
		ChemComp comp = new ChemComp();

		comp.setOne_letter_code("?");
		comp.setThree_letter_code("???"); // Main signal for isEmpty()
		comp.setPolymerType(PolymerType.unknown);
		comp.setResidueType(ResidueType.atomn);
		return comp;
	}

	/**
	 * Indicates whether this compound was created with
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		// Is this the best flag for it being empty?
		return id == null || getThree_letter_code() == null || "???".equals(getThree_letter_code());
	}

}

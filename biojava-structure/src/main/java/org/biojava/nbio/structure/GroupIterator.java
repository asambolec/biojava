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
 * Created on 09.05.2004
 * @author Andreas Prlic
 *
 */

package org.biojava.nbio.structure;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An iterator over all groups of a structure.
 * 
 * @author Andreas Prlic
 * @since 1.4
 * @version %I% %G%
 */

public class GroupIterator implements Iterator<Group> {

	private Structure structure;
	private int currentModelPos;
	private int currentChainPos;
	private int currentGroupPos;
	private boolean fixedModel;

	/**
	 * Constructs a GroupIterator object over all models
	 *
	 * @param struct a Structure object
	 */
	public GroupIterator(Structure struct) {
		structure = struct;
		currentModelPos = 0;
		currentChainPos = 0;
		currentGroupPos = -1;
		fixedModel = false;
	}

	/**
	 * Constructs a GroupIterator object over a specific model
	 *
	 * @param struct a Structure object
	 */
	public GroupIterator(Structure struct, int modelNr) {
		structure = struct;
		currentModelPos = modelNr;
		currentChainPos = 0;
		currentGroupPos = -1;
		fixedModel = true;
	}

	/** needed to do a copy of iterator ... */
	private Structure getStructure() {
		return structure;
	}

	private int getModelPos() {
		return currentModelPos;
	}

	private void setModelPos(int pos) {
		currentModelPos = pos;
	}

	private int getChainPos() {
		return currentChainPos;
	}

	private void setChainPos(int pos) {
		currentChainPos = pos;
	}

	private int getGroupPos() {
		return currentGroupPos;
	}

	private void setGroupPos(int pos) {
		currentGroupPos = pos;
	}

	/** Creates and returns a copy of this object. */
	@Override
	public Object clone() {

		GroupIterator gr = new GroupIterator(this.getStructure());
		gr.setModelPos(this.getModelPos());
		gr.setChainPos(this.getChainPos());
		gr.setGroupPos(this.getGroupPos());
		gr.fixedModel = this.fixedModel;
		return gr;

	}

	/** is there a group after the current one in the structure? */
	@Override
	public boolean hasNext() {
		return hasSubGroup(currentModelPos, currentChainPos, currentGroupPos + 1);
	}

	/**
	 * recursive method to determine if there is a next group. Helper method for
	 * hasNext().
	 * 
	 * @see #hasNext
	 */
	private boolean hasSubGroup(int tmp_model, int tmp_chain, int tmp_group) {

		if (tmp_model >= structure.nrModels()) {
			return false;
		}

		List<Chain> model = structure.getModel(tmp_model);

		if (tmp_chain >= model.size()) {
			if (fixedModel) {
				return false;
			}
			return hasSubGroup(tmp_model + 1, 0, 0);
		}

		Chain chain = model.get(tmp_chain);

		// start search at beginning of next chain.
		return tmp_group < chain.getAtomLength() || hasSubGroup(tmp_model, tmp_chain + 1, 0);

	}

	/**
	 * Get the model number of the current model.
	 *
	 * @return the number of the model
	 */
	public int getCurrentModel() {

		return currentModelPos;
	}

	/**
	 * Get the current Chain. Returns null if we are at the end of the iteration.
	 *
	 * @return the Chain of the current position
	 */
	public Chain getCurrentChain() {
		if (currentModelPos >= structure.nrModels()) {
			return null;
		}

		List<Chain> model = structure.getModel(currentModelPos);

		if (currentChainPos >= model.size()) {
			return null;
		}

		return model.get(currentChainPos);

	}

	/**
	 * get next Group.
	 * 
	 * @return next Group
	 * @throws NoSuchElementException ...
	 */
	@Override
	public Group next() {

		return getNextGroup(currentModelPos, currentChainPos, currentGroupPos + 1);
	}

	/**
	 * recursive method to retrieve the next group. Helper method for gext().
	 * 
	 * @see #next
	 */
	private Group getNextGroup(int tmp_model, int tmp_chain, int tmp_group) {

		if (tmp_model >= structure.nrModels()) {
			throw new NoSuchElementException("arrived at end of structure!");
		}

		List<Chain> model = structure.getModel(tmp_model);

		if (tmp_chain >= model.size()) {
			if (fixedModel) {
				throw new NoSuchElementException("arrived at end of model!");
			}
			return getNextGroup(tmp_model + 1, 0, 0);
		}

		Chain chain = model.get(tmp_chain);

		if (tmp_group >= chain.getAtomLength()) {
			// start search at beginning of next chain.
			return getNextGroup(tmp_model, tmp_chain + 1, 0);
		} else {
			currentModelPos = tmp_model;
			currentChainPos = tmp_chain;
			currentGroupPos = tmp_group;
			return chain.getAtomGroup(currentGroupPos);
		}

	}

	/**
	 * does nothing .
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot call remove() for GroupIterator");
	}

}

/**
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
 * Created on 5 Mar 2013
 * Created by Andreas Prlic
 *
 * @since 3.0.6
 */
package org.biojava.nbio.structure.math;

import java.io.Serializable;

/**
 *
 * A sparse, square matrix, implementing using two arrays of sparse vectors, one
 * representation for the rows and one for the columns.
 *
 * For matrix-matrix product, we might also want to store the column
 * representation.
 *
 * Derived from http://introcs.cs.princeton.edu/java/44st/SparseMatrix.java.html
 *
 * For additional documentation, see
 * <a href="http://introcs.cs.princeton.edu/44st">Section 4.4</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 *
 *
 **/

public class SparseSquareMatrix implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5217767192992868955L;

	private final int n; // N-by-N matrix
	private SparseVector[] rows; // the rows, each row is a sparse vector

	/**
	 * initialize an N-by-N matrix of all 0s
	 *
	 * @param N - size
	 */
	public SparseSquareMatrix(int N) {
		this.n = N;
		rows = new SparseVector[N];

		for (int i = 0; i < N; i++) {
			rows[i] = new SparseVector(N);
		}
	}

	/**
	 * set A[i][j] = value
	 *
	 * @param i
	 * @param j
	 * @param value
	 */
	public void put(int i, int j, double value) {

		if (i < 0 || i >= n) {
			throw new IllegalArgumentException("Illegal index");
		}
		if (j < 0 || j >= n) {
			throw new IllegalArgumentException("Illegal index");
		}

		rows[i].put(j, value);
	}

	/**
	 * access a value at i,j
	 *
	 * @param i
	 * @param j
	 * @return return A[i][j]
	 */
	public double get(int i, int j) {

		if (i < 0 || i >= n) {
			throw new IllegalArgumentException(new StringBuilder().append("Illegal index ").append(i)
					.append(" should be > 0 and < ").append(n).toString());
		}
		if (j < 0 || j >= n) {
			throw new IllegalArgumentException(new StringBuilder().append("Illegal index ").append(j)
					.append(" should be > 0 and < ").append(n).toString());
		}

		return rows[i].get(j);
	}

	/**
	 * return the number of nonzero entries (not the most efficient implementation)
	 *
	 * @return
	 */
	public int nnz() {

		int sum = 0;

		for (int i = 0; i < n; i++) {
			sum += rows[i].nnz();
		}

		return sum;
	}

	/**
	 *
	 * @param x
	 * @return return the matrix-vector product b = Ax
	 */
	public SparseVector times(SparseVector x) {

		SparseSquareMatrix A = this;

		if (n != x.size()) {
			throw new IllegalArgumentException(new StringBuilder().append("Dimensions disagree. ").append(n)
					.append(" != ").append(x.size()).toString());
		}

		SparseVector b = new SparseVector(n);

		for (int i = 0; i < n; i++) {
			b.put(i, A.rows[i].dot(x));
		}

		return b;
	}

	/**
	 * return C = A + B
	 *
	 * @param B
	 * @return
	 */
	public SparseSquareMatrix plus(SparseSquareMatrix B) {

		SparseSquareMatrix A = this;

		if (A.n != B.n) {
			throw new IllegalArgumentException(new StringBuilder().append("Dimensions disagree. ").append(A.n)
					.append(" != ").append(B.n).toString());
		}

		SparseSquareMatrix C = new SparseSquareMatrix(n);

		for (int i = 0; i < n; i++) {
			C.rows[i] = A.rows[i].plus(B.rows[i]);
		}

		return C;
	}

	@Override
	public String toString() {

		StringBuilder s = new StringBuilder();

		s.append("N = ");
		s.append(n);
		s.append(", nonzeros = ");
		s.append(nnz());
		s.append(System.getProperty("line.separator"));
		for (int i = 0; i < n; i++) {
			s.append(i);
			s.append(": ");
			s.append(rows[i]);
			s.append(System.getProperty("line.separator"));
		}

		return s.toString();
	}

}

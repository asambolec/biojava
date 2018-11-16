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
package org.biojava.nbio.structure.jama;

/**
 * QR Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
 * orthogonal matrix Q and an n-by-n upper triangular matrix R so that A = Q*R.
 * <P>
 * The QR decompostion always exists, even if the matrix does not have full
 * rank, so the constructor will never fail. The primary use of the QR
 * decomposition is in the least squares solution of nonsquare systems of
 * simultaneous linear equations. This will fail if isFullRank() returns false.
 */

public class QRDecomposition implements java.io.Serializable {

	static final long serialVersionUID = 10293720387423l;

	/*
	 * ------------------------ Class variables ------------------------
	 */

	/**
	 * Array for internal storage of decomposition.
	 * 
	 * @serial internal array storage.
	 */
	private double[][] qr;

	/**
	 * Row and column dimensions.
	 * 
	 * @serial column dimension.
	 * @serial row dimension.
	 */
	private int m;

	/**
	 * Row and column dimensions.
	 * 
	 * @serial column dimension.
	 * @serial row dimension.
	 */
	private int n;

	/**
	 * Array for internal storage of diagonal of R.
	 * 
	 * @serial diagonal of R.
	 */
	private double[] rdiag;

	/*
	 * ------------------------ Constructor ------------------------
	 */

	/**
	 * QR Decomposition, computed by Householder reflections. provides a data
	 * structure to access R and the Householder vectors and compute Q.
	 * 
	 * @param A Rectangular matrix
	 */

	public QRDecomposition(Matrix A) {
		// Initialize.
		qr = A.getArrayCopy();
		m = A.getRowDimension();
		n = A.getColumnDimension();
		rdiag = new double[n];

		// Main loop.
		for (int k = 0; k < n; k++) {
			// Compute 2-norm of k-th column without under/overflow.
			double nrm = 0;
			for (int i = k; i < m; i++) {
				nrm = Maths.hypot(nrm, qr[i][k]);
			}

			if (nrm != 0.0) {
				// Form k-th Householder vector.
				if (qr[k][k] < 0) {
					nrm = -nrm;
				}
				for (int i = k; i < m; i++) {
					qr[i][k] /= nrm;
				}
				qr[k][k] += 1.0;

				// Apply transformation to remaining columns.
				for (int j = k + 1; j < n; j++) {
					double s = 0.0;
					for (int i = k; i < m; i++) {
						s += qr[i][k] * qr[i][j];
					}
					s = -s / qr[k][k];
					for (int i = k; i < m; i++) {
						qr[i][j] += s * qr[i][k];
					}
				}
			}
			rdiag[k] = -nrm;
		}
	}

	/*
	 * ------------------------ Public Methods ------------------------
	 */

	/**
	 * Is the matrix full rank?
	 * 
	 * @return true if R, and hence A, has full rank.
	 */

	public boolean isFullRank() {
		for (int j = 0; j < n; j++) {
			if (rdiag[j] == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return the Householder vectors
	 * 
	 * @return Lower trapezoidal matrix whose columns define the reflections
	 */

	public Matrix getH() {
		Matrix X = new Matrix(m, n);
		double[][] H = X.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i >= j) {
					H[i][j] = qr[i][j];
				} else {
					H[i][j] = 0.0;
				}
			}
		}
		return X;
	}

	/**
	 * Return the upper triangular factor
	 * 
	 * @return R
	 */

	public Matrix getR() {
		Matrix X = new Matrix(n, n);
		double[][] R = X.getArray();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i < j) {
					R[i][j] = qr[i][j];
				} else if (i == j) {
					R[i][j] = rdiag[i];
				} else {
					R[i][j] = 0.0;
				}
			}
		}
		return X;
	}

	/**
	 * Generate and return the (economy-sized) orthogonal factor
	 * 
	 * @return Q
	 */

	public Matrix getQ() {
		Matrix X = new Matrix(m, n);
		double[][] Q = X.getArray();
		for (int k = n - 1; k >= 0; k--) {
			for (int i = 0; i < m; i++) {
				Q[i][k] = 0.0;
			}
			Q[k][k] = 1.0;
			for (int j = k; j < n; j++) {
				if (qr[k][k] != 0) {
					double s = 0.0;
					for (int i = k; i < m; i++) {
						s += qr[i][k] * Q[i][j];
					}
					s = -s / qr[k][k];
					for (int i = k; i < m; i++) {
						Q[i][j] += s * qr[i][k];
					}
				}
			}
		}
		return X;
	}

	/**
	 * Least squares solution of A*X = B
	 * 
	 * @param B A Matrix with as many rows as A and any number of columns.
	 * @return X that minimizes the two norm of Q*R*X-B.
	 * @exception IllegalArgumentException Matrix row dimensions must agree.
	 * @exception RuntimeException         Matrix is rank deficient.
	 */

	public Matrix solve(Matrix B) {
		if (B.getRowDimension() != m) {
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		}
		if (!this.isFullRank()) {
			throw new RuntimeException("Matrix is rank deficient.");
		}

		// Copy right hand side
		int nx = B.getColumnDimension();
		double[][] X = B.getArrayCopy();

		// Compute Y = transpose(Q)*B
		for (int k = 0; k < n; k++) {
			for (int j = 0; j < nx; j++) {
				double s = 0.0;
				for (int i = k; i < m; i++) {
					s += qr[i][k] * X[i][j];
				}
				s = -s / qr[k][k];
				for (int i = k; i < m; i++) {
					X[i][j] += s * qr[i][k];
				}
			}
		}
		// Solve R*X = Y;
		for (int k = n - 1; k >= 0; k--) {
			for (int j = 0; j < nx; j++) {
				X[k][j] /= rdiag[k];
			}
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < nx; j++) {
					X[i][j] -= X[k][j] * qr[i][k];
				}
			}
		}
		return (new Matrix(X, n, nx).getMatrix(0, n - 1, 0, nx - 1));
	}
}

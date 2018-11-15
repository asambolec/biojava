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
 * Created on 2012-11-20
 *
 */

package org.biojava.nbio.structure;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A chainName, a start residue, and an end residue. The chainName is matched to
 * {@link Chain#getName()}, so for mmCIF files it indicates the authorId rather
 * than the asymId.
 *
 * Chain may be null when referencing a single-chainName structure; for
 * multi-chainName structures omitting the chainName is an error. Start and/or
 * end may also be null, which is interpreted as the first and last residues in
 * the chainName, respectively.
 *
 * @author dmyerstu
 * @see ResidueNumber
 * @see org.biojava.nbio.structure.ResidueRangeAndLength
 */
public class ResidueRange {

	public static final Pattern RANGE_REGEX = Pattern.compile(new StringBuilder().append("^\\s*([a-zA-Z0-9]+|_)")
			.append("(?:").append("(?::|_|:$|_$|$)").append("(?:").append("([-+]?[0-9]+[A-Za-z]?|\\^)?").append("(?:")
			.append("\\s*(-)\\s*").append("([-+]?[0-9]+[A-Za-z]?|\\$)?").append(")?+").append(")?+").append(")?")
			.append("\\s*").toString());
	public static final Pattern CHAIN_REGEX = Pattern.compile("^\\s*([a-zA-Z0-9]+|_)$");
	private final String chainName;
	private final ResidueNumber end;
	private final ResidueNumber start;

	public ResidueRange(String chainName, String start, String end) {
		this.chainName = chainName;
		this.start = ResidueNumber.fromString(start);
		this.start.setChainName(chainName);
		this.end = ResidueNumber.fromString(end);
		this.end.setChainName(chainName);
	}

	public ResidueRange(String chainName, ResidueNumber start, ResidueNumber end) {
		this.chainName = chainName;
		this.start = start;
		this.end = end;
	}

	/**
	 * Parse the residue range from a string. Several formats are accepted:
	 * <ul>
	 * <li>chainName.start-end
	 * <li>chainName.residue
	 * <li>chain_start-end (for better filename compatibility)
	 * </ul>
	 *
	 * <p>
	 * Residues can be positive or negative and may include insertion codes. See
	 * {@link ResidueNumber#fromString(String)}.
	 *
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>A:5-100</code>
	 * <li><code>A_5-100</code>
	 * <li><code>A_-5</code>
	 * <li><code>A:-12I-+12I</code>
	 * <li><code>A:^-$</code>
	 * </ul>
	 *
	 * @param s residue string to parse
	 * @return The unique ResidueRange corresponding to {@code s}
	 */
	public static ResidueRange parse(String s) {
		Matcher matcher = RANGE_REGEX.matcher(s);
		if (matcher.matches()) {
			ResidueNumber start = null;
			ResidueNumber end = null;
			String chain = null;
			try {
				chain = matcher.group(1);
				if (matcher.group(2) != null) {
					// ^ indicates first res (start==null)
					if (!"^".equals(matcher.group(2))) {
						start = ResidueNumber.fromString(matcher.group(2));
						start.setChainName(chain);
					}
				}
				if (matcher.group(3) == null) {
					// single-residue range
					end = start;
				} else
				// $ indicates last res (end==null)
				if (matcher.group(4) != null && !"$".equals(matcher.group(4))) {
					end = ResidueNumber.fromString(matcher.group(4));
					end.setChainName(chain);
				}

				return new ResidueRange(chain, start, end);
			} catch (IllegalStateException e) {
				throw new IllegalArgumentException(
						new StringBuilder().append("Range ").append(s).append(" was not valid").toString(), e);
			}
		} else if (CHAIN_REGEX.matcher(s).matches()) {
			return new ResidueRange(s, (ResidueNumber) null, null);
		}
		throw new IllegalArgumentException("Illegal ResidueRange format:" + s);
	}

	/**
	 * @param s A string of the form chain_start-end,chain_start-end, ... For
	 *          example: <code>A.5-100,R_110-190,Z_200-250</code>.
	 * @return The unique ResidueRange corresponding to {@code s}.
	 * @see #parse(String)
	 */
	public static List<ResidueRange> parseMultiple(String s) {
		s = s.trim();
		// trim parentheses, for backwards compatibility
		if (s.startsWith("(")) {
			s = s.substring(1);
		}
		if (s.endsWith(")")) {
			s = s.substring(0, s.length() - 1);
		}

		String[] parts = s.split(",");
		List<ResidueRange> list = new ArrayList<>(parts.length);
		for (String part : parts) {
			list.add(parse(part));
		}
		return list;
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
		ResidueRange other = (ResidueRange) obj;
		if (chainName == null) {
			if (other.chainName != null) {
				return false;
			}
		} else if (!chainName.equals(other.chainName)) {
			return false;
		}
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		return true;
	}

	public String getChainName() {
		return chainName;
	}

	public ResidueNumber getEnd() {
		return end;
	}

	public ResidueNumber getStart() {
		return start;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (chainName == null ? 0 : chainName.hashCode());
		result = prime * result + (end == null ? 0 : end.hashCode());
		result = prime * result + (start == null ? 0 : start.hashCode());
		return result;
	}

	@Override
	public String toString() {
		if (start == null && end == null) {
			// Indicates the full chainName
			return chainName;
		}
		return new StringBuilder().append(chainName).append("_").append(start).append("-").append(end).toString();
	}

	/**
	 * Returns the ResidueNumber that is at position {@code positionInRange} in
	 * <em>this</em> ResidueRange.
	 * 
	 * @return The ResidueNumber, or false if it does not exist or is not within
	 *         this ResidueRange
	 */
	public ResidueNumber getResidue(int positionInRange, AtomPositionMap map) {
		if (map == null) {
			throw new NullPointerException("The AtomPositionMap must be non-null");
		}
		int i = 0;
		for (Map.Entry<ResidueNumber, Integer> entry : map.getNavMap().entrySet()) {
			if (i == positionInRange) {
				return entry.getKey();
			}
			if (contains(entry.getKey(), map)) {
				i++;
			}
		}
		return null;
	}

	/**
	 * @return True if and only if {@code residueNumber} is within this ResidueRange
	 */
	public boolean contains(ResidueNumber residueNumber, AtomPositionMap map) {

		if (residueNumber == null) {
			throw new NullPointerException("Can't find the ResidueNumber because it is null");
		}

		if (map == null) {
			throw new NullPointerException("The AtomPositionMap must be non-null");
		}

		Integer pos = map.getPosition(residueNumber);
		if (pos == null) {
			throw new IllegalArgumentException("Couldn't find residue " + residueNumber.printFull());
		}

		ResidueNumber startResidue = getStart() == null ? map.getFirst(getChainName()) : getStart();
		Integer startPos = map.getPosition(startResidue);
		if (startPos == null) {
			throw new IllegalArgumentException("Couldn't find the start position");
		}

		ResidueNumber endResidue = getEnd() == null ? map.getLast(getChainName()) : getEnd();
		Integer endPos = map.getPosition(endResidue);
		if (endPos == null) {
			throw new IllegalArgumentException("Couldn't find the end position");
		}
		return pos >= startPos && pos <= endPos;
	}

	/**
	 * Returns a new Iterator over every {@link ResidueNumber} in this ResidueRange.
	 * Stores the contents of {@code map} until the iterator is finished, so calling
	 * code should set the iterator to {@code null} if it did not finish.
	 */
	public Iterator<ResidueNumber> iterator(final AtomPositionMap map) {
		// Use Entries to guarentee not null
		final Iterator<Entry<ResidueNumber, Integer>> entryIt = map.getNavMap().entrySet().iterator();
		if (!entryIt.hasNext()) {
			// empty iterator
			return Arrays.asList(new ResidueNumber[0]).iterator();
		}
		// Peek at upcoming entry

		return new Iterator<ResidueNumber>() {
			Entry<ResidueNumber, Integer> next = loadNext();

			private Entry<ResidueNumber, Integer> loadNext() {

				while (entryIt.hasNext()) {
					next = entryIt.next();
					ResidueNumber nextPos = next.getKey();
					if (contains(nextPos, map)) {
						// loaded a valid next value
						return next;
					}
				}
				next = null;
				return next;
			}

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public ResidueNumber next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				ResidueNumber pos = next.getKey();
				loadNext();
				return pos;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not modifiable");
			}
		};
	}

	/**
	 * Returns a new Iterator over every {@link ResidueNumber} in the list of
	 * ResidueRanges. Stores the contents of {@code map} until the iterator is
	 * finished, so calling code should set the iterator to {@code null} if it did
	 * not finish.
	 */
	public static Iterator<ResidueNumber> multiIterator(final AtomPositionMap map, final ResidueRange... rrs) {
		return new Iterator<ResidueNumber>() {
			private int r = 0;
			private Iterator<ResidueNumber> internal;

			@Override
			public boolean hasNext() {
				if (!(r == rrs.length - 1)) {
					return true;
				}
				init();
				return internal.hasNext();
			}

			private void init() {
				if (internal == null) {
					internal = rrs[r].iterator(map);
				}
			}

			@Override
			public ResidueNumber next() {
				if (rrs.length == 0) {
					throw new NoSuchElementException();
				}
				init();
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				if (!internal.hasNext()) {
					r++;
					internal = rrs[r].iterator(map);
				}
				return internal.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not modifiable");
			}
		};
	}

	/**
	 * Returns a new Iterator over every {@link ResidueNumber} in the list of
	 * ResidueRanges. Stores the contents of {@code map} until the iterator is
	 * finished, so calling code should set the iterator to {@code null} if it did
	 * not finish.
	 */
	public static Iterator<ResidueNumber> multiIterator(AtomPositionMap map, List<? extends ResidueRange> rrs) {
		ResidueRange[] ranges = new ResidueRange[rrs.size()];
		for (int i = 0; i < rrs.size(); i++) {
			ranges[i] = rrs.get(i);
		}
		return multiIterator(map, ranges);
	}

	public static List<ResidueRange> parseMultiple(List<String> ranges) {
		List<ResidueRange> rrs = new ArrayList<>(ranges.size());
		ranges.stream().map(ResidueRange::parse).forEach(rr -> {
			if (rr != null) {
				rrs.add(rr);
			}
		});
		return rrs;
	}

	public static List<String> toStrings(List<? extends ResidueRange> ranges) {
		List<String> list = new ArrayList<>(ranges.size());
		ranges.forEach(range -> list.add(range.toString()));
		return list;
	}

	public static String toString(List<? extends ResidueRange> ranges) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ranges.size(); i++) {
			sb.append(ranges.get(i));
			if (i < ranges.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

}

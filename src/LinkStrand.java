import java.util.HashMap;
import java.util.Iterator;

public class LinkStrand implements IDnaStrand, Iterator<String> {

	private class Node {
		public String string;
		public Node next;
		Node(){
			this("",null);
		}
		Node(String s){
			this(s,null);
		}
		@SuppressWarnings("unused")
		Node(Node n){
			this("",n);
		}
		Node(String s, Node n) {
			string = s;
			next = n;
		}
		@SuppressWarnings("unused")
		public boolean hasNext(){
			return next != null;
		}
	}
	private int myAppends;
	private long size;
	private Node first;
	private Node current;
	private Node last;

	/**
	 * Create a strand representing an empty DNA strand, length of zero.
	 */
	public LinkStrand() {
		this("");
	}

	/**
	 * Create a strand representing s. No error checking is done to see if s
	 * represents valid genomic/DNA data.
	 * 
	 * @param s
	 *            is the source of cgat data for this strand
	 */
	public LinkStrand(String s) {
		initialize(s);
	}

	/**
	 * Initialize this strand so that it represents the value of source. No
	 * error checking is performed.
	 * 
	 * @param source
	 *            is the source of this enzyme
	 */
	@Override
	public void initialize(String source) {
		first = new Node(source);
		current = first;
		last = first;
		myAppends = 0;
		size = source.length();
	}

	/**
	 * Cut this strand at every occurrence of enzyme, essentially replacing
	 * every occurrence of enzyme with splicee.
	 * 
	 * @param enzyme
	 *            is the pattern/strand searched for and replaced
	 * @param splicee
	 *            is the pattern/strand replacing each occurrence of enzyme
	 * @return the new strand leaving the original strand unchanged.
	 */
	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		StringBuilder search = new StringBuilder(toString());
		boolean first = true;
		LinkStrand ret = null;

		/*
		 * The next line is very syntax-dense. .indexOf looks for the first
		 * index at which enzyme occurs, starting at pos. Saying pos = ...
		 * assigns the result of that operation to the pos variable; the value
		 * of pos is then compared against zero.
		 * 
		 * .indexOf returns -1 if enzyme can't be found. Therefore, this line
		 * is:
		 * 
		 * "While I can find enzyme, assign the location where it occurs to pos,
		 * and then execute the body of the loop."
		 */
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = new LinkStrand(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			// NOTE: This is an important special case! If the enzyme
			// is never found, return an empty String.
			if (ret == null) {
				ret = new LinkStrand("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	/**
	 * Returns the number of nucleotides/base-pairs in this strand.
	 */
	@Override
	public long size() {
		return size;
	}

	/**
	 * Return some string identifying this class.
	 * 
	 * @return a string representing this strand and its characteristics
	 */
	@Override
	public String strandInfo() {
		return this.getClass().getName();
	}

	/**
	 * Returns a string that can be printed to reveal information about what
	 * this object has encountered as it is manipulated by append and
	 * cutAndSplice.
	 * 
	 * @return
	 */
	@Override
	public String getStats() {
		return String.format("# append calls = %d", myAppends);
	}

	/**
	 * Returns the sequence of DNA this object represents as a String
	 * 
	 * @return the sequence of DNA this represents
	 */
	@Override
	public String toString() {
		Node localCurrent = first;
		String toReturn = "";
		while (localCurrent != null){
			toReturn += localCurrent.string;
			localCurrent = localCurrent.next;
		}
		return toReturn;
	}

	/**
	 * Append a strand of DNA to this strand. If the strand being appended is
	 * represented by a LinkStrand object then an efficient append is performed.
	 * 
	 * @param dna
	 *            is the strand being appended
	 */
	@Override
	public void append(IDnaStrand dna) {
		String toAppend = "";
		if (dna instanceof SimpleStrand) {
			SimpleStrand ss = (SimpleStrand) dna;
			toAppend = ss.toString();
		} else if (dna instanceof LinkStrand) {
			LinkStrand ls = (LinkStrand) dna;
			toAppend = ls.toString();
		} else {
			toAppend = dna.toString();
		}
		append(toAppend);
	}

	/**
	 * Simply append a strand of dna data to this strand.
	 * 
	 * @param dna
	 *            is the String appended to this strand
	 */
	@Override
	public void append(String dna) {
		last.next = new Node(dna);
		last = last.next;
		last.next = new Node();
		size += dna.length();
		myAppends++;
	}

	/**
	 * Returns an IDnaStrand that is the reverse of this strand, e.g., for
	 * "CGAT" returns "TAGC"
	 * 
	 * @return reverse strand
	 */
	@Override
	public IDnaStrand reverse() {
		LinkStrand copy = new LinkStrand(toString());
		HashMap<String,String> reverses = new HashMap<String,String>();
		if (copy.first != null) {
			Node revFirst = reverseRecursive(copy.first,reverses);
			StringBuilder rev = new StringBuilder();
			while (revFirst != null) {
				rev.append(revFirst.string);
				revFirst = revFirst.next;
			}
			return new LinkStrand(rev.toString());
		} else {
			return new LinkStrand();
		}
	}

	private Node reverseRecursive(Node list, HashMap<String,String> reverses){
		if (list.next != null){
			Node answer = reverseRecursive(list.next,reverses);
			list.next.next = list;
			list.next = null;
			return answer;
		} else {
			if (!reverses.containsKey(list.string)){
				reverses.put(list.string, new StringBuilder(list.string).reverse().toString());
			}
			return new Node(reverses.get(list.string), list.next);
		}
	}

	/**
	 *	Returns the next element in the underlying LinkedList data structure
	 */
	@Override
	public String next() {
		String toReturn = current.string;
		current = current.next;
		return toReturn;
	}

	/**
	 *	True if next evaluates correctly
	 *	False if next returns with an error
	 */
	@Override
	public boolean hasNext() {
		return current.next != null;
	}
}
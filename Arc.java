public class Arc implements Comparable<Arc> {
    Choreographer Xi, Xj;

    public Arc(Choreographer choreo_i, Choreographer choreo_j) {
        if (choreo_i.equals(choreo_j)) {
            try {
                throw new Exception(choreo_i + " is equal to " + choreo_j);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        Xi = choreo_i;
        Xj = choreo_j;
    }

    @Override
    public int compareTo(Arc otherArc) {
        // Assuming Choreographer implements Comparable
        int compareXi = this.Xi.compareTo(otherArc.Xi);
        if (compareXi != 0) {
            return compareXi;
        }
        return this.Xj.compareTo(otherArc.Xj);
    }

    @Override
    public String toString() {
        return "(" + Xi + "," + Xj + ")";
    }
}
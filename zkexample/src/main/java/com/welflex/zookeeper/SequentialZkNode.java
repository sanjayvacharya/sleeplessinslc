package com.welflex.zookeeper;

/**
 * Sequential Z Node based of the code from:
 * http://techblog.outbrain.com/2011/07/leader-election-with-zookeeper/
 */
class SequentialZkNode implements Comparable<SequentialZkNode> {
  private final String fullPath;

  private final Integer sequence;

  public SequentialZkNode(String fullPath, Integer sequence) {
    this.fullPath = fullPath;
    this.sequence = sequence;
  }

  public String getPath() {
    return fullPath;
  }

  public Integer getSequence() {
    return sequence;
  }

  @Override public int compareTo(SequentialZkNode other) {
    return sequence.compareTo(other.sequence);
  }
 
  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fullPath == null)
        ? 0
        : fullPath.hashCode());
    result = prime * result + ((sequence == null)
        ? 0
        : sequence.hashCode());
    return result;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SequentialZkNode other = (SequentialZkNode) obj;
    if (fullPath == null) {
      if (other.fullPath != null)
        return false;
    }
    else if (!fullPath.equals(other.fullPath))
      return false;
    if (sequence == null) {
      if (other.sequence != null)
        return false;
    }
    else if (!sequence.equals(other.sequence))
      return false;
    return true;
  }

  @Override public String toString() {
    return "SequentialZkNode [fullPath=" + fullPath + ", sequence=" + sequence + "]";
  }
}

package com.welflex.zookeeper;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;

/**
 * Based of the code from : http://techblog.outbrain.com/2011/07/leader-election-with-zookeeper/
 */
public class ZkUtils {
  private static final String PATH_SEPARATOR = "/";
  private static final String SEQUENCE_SEPARATOR = "_";

  public static NavigableSet<SequentialZkNode> getNodes(String path, ZkClient zkClient) {
    List<String> children = zkClient.getChildren(path);

    final NavigableSet<SequentialZkNode> nodes = new TreeSet<SequentialZkNode>();

    for (final String child : children) {
      final String fullPath = path + PATH_SEPARATOR + child;
      nodes.add(new SequentialZkNode(fullPath, parseSequenceFromName(child)));
    }

    return nodes;
  }

  public static int parseSequenceFromName(final String path) {
    if (StringUtils.isEmpty(path)) {
      throw new IllegalArgumentException("path value cannot be empty");
    }
    final int idx = path.lastIndexOf(SEQUENCE_SEPARATOR);
    final String sequence = path.substring(idx + 1, path.length());
    return Integer.valueOf(sequence);
  }

  public static SequentialZkNode findLeaderOfNode(final NavigableSet<SequentialZkNode> nodes,
    SequentialZkNode node) {
    if (null == nodes || nodes.isEmpty()) {
      throw new IllegalArgumentException("Cannot use an empty or null list to find a leader");
    }

    if (nodes.size() > 1) {
      final SequentialZkNode leader = nodes.lower(node);
      
      if (null != leader) {
        return leader;
      }
    }
    return nodes.first();
  }
  
  public static SequentialZkNode createEpheremalNode(String rootPath, Object data, ZkClient zkClient) {
    String createdPath = zkClient.createEphemeralSequential(rootPath + "/" + "n_", data);
    Integer sequence = ZkUtils.parseSequenceFromName(createdPath);
    return new SequentialZkNode(createdPath, sequence);
  }
}

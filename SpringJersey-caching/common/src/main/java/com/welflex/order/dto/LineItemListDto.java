package com.welflex.order.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LineItemListDto implements Iterable<LineItemDto> {
  private List<LineItemDto> lineItemDtos;
  
  public LineItemListDto(Set<LineItemDto> items) {
    this.lineItemDtos = new ArrayList<LineItemDto>();
    lineItemDtos.addAll(items);
  }
  public Iterator<LineItemDto> iterator() {
    return lineItemDtos.iterator();
  }

  public List<LineItemDto> getLineItemDtos() {
    return lineItemDtos;
  }

  public void setLineItemDtos(List<LineItemDto> lineItemDtos) {
    this.lineItemDtos = lineItemDtos;
  }
}

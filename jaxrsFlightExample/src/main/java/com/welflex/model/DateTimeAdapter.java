package com.welflex.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

  @Override
  public String marshal(DateTime v) throws Exception {
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd:hh:mm");
    return fmt.print(v);
  }

  @Override
  public DateTime unmarshal(String v) throws Exception {
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd:hh:mm");
    return fmt.parseDateTime(v);
  }
}

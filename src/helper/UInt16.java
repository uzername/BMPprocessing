/*
 * D-Bus Java Implementation Copyright (c) 2005-2006 Matthew Johnson This
 * program is free software; you can redistribute it and/or modify it under the
 * terms of either the GNU Lesser General Public License Version 2 or the
 * Academic Free Licence Version 2.1. Full licence texts are included in the
 * COPYING file with this program.
 */
package helper;

import java.text.MessageFormat;

/**
 * Class to represent unsigned 16-bit numbers.
 */
@SuppressWarnings("serial")
public class UInt16 extends Number implements Comparable<UInt16> {
  /** Maximum allowed value */
  public static final int MAX_VALUE = 65535;
  /** Minimum allowed value */
  public static final int MIN_VALUE = 0;
  private int             value;

  /**
   * Create a UInt16 from a int.
   * 
   * @param value
   *          Must be a valid integer within MIN_VALUE&ndash;MAX_VALUE
   * @throws NumberFormatException
   *           if value is not between MIN_VALUE and MAX_VALUE
   */
  public UInt16(int value) {
    this.value = value;
  }

  /**
   * Create a UInt16 from a String.
   * 
   * @param value
   *          Must parse to a valid integer within MIN_VALUE&ndash;MAX_VALUE
   * @throws NumberFormatException
   *           if value is not an integer between MIN_VALUE and MAX_VALUE
   */
  public UInt16(String value) {
    this(Integer.parseInt(value));
  }

  /**
   * assemble number from array with 2 bytes: last one is MSB, first one is LSB
   * @param in_bytes 
   */
  public void AssignUInt16FromByteArrayLSBFirst(int[] in_bytes) {
      this.value = 0;
      this.value = this.value | ((int) in_bytes[0]);
      this.value = this.value | (((int) in_bytes[1])<<8);
      
  }
  
  /** The value of this as a byte. */
  @Override
  public byte byteValue() {
    return (byte) value;
  }

  /**
   * Compare two UInt16s.
   * 
   * @return 0 if equal, -ve or +ve if they are different.
   */
  public int compareTo(UInt16 other) {
    return (int) (value - other.value);
  }

  /** The value of this as a double. */
  @Override
  public double doubleValue() {
    return value;
  }

  /** Test two UInt32s for equality. */
  @Override
  public boolean equals(Object o) {
    return (o instanceof UInt16) && (((UInt16) o).value == value);
  }

  /** The value of this as a float. */
  @Override
  public float floatValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return (int) value;
  }

  /** The value of this as a int. */
  @Override
  public int intValue() {
    return /*(int)*/ value;
  }

  /** The value of this as a long. */
  @Override
  public long longValue() {
    return (long)value;
  }

  /** The value of this as a short. */
  @Override
  public short shortValue() {
    return (short) value;
  }

  /** The value of this as a string */
  @Override
  public String toString() {
    return "" + value;
  }
}
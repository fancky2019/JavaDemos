// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: money.proto

package com.google.type;

/**
 * <pre>
 * Represents an amount of money with its currency type.
 * </pre>
 *
 * Protobuf type {@code google.type.Money}
 */
public final class Money extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.type.Money)
    MoneyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Money.newBuilder() to construct.
  private Money(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Money() {
    currencyCode_ = "";
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new Money();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Money(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            String s = input.readStringRequireUtf8();

            currencyCode_ = s;
            break;
          }
          case 16: {

            units_ = input.readInt64();
            break;
          }
          case 24: {

            nanos_ = input.readInt32();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return MoneyProto.internal_static_google_type_Money_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return MoneyProto.internal_static_google_type_Money_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            Money.class, Builder.class);
  }

  public static final int CURRENCY_CODE_FIELD_NUMBER = 1;
  private volatile Object currencyCode_;
  /**
   * <pre>
   * The three-letter currency code defined in ISO 4217.
   * </pre>
   *
   * <code>string currency_code = 1;</code>
   * @return The currencyCode.
   */
  @Override
  public String getCurrencyCode() {
    Object ref = currencyCode_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      currencyCode_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * The three-letter currency code defined in ISO 4217.
   * </pre>
   *
   * <code>string currency_code = 1;</code>
   * @return The bytes for currencyCode.
   */
  @Override
  public com.google.protobuf.ByteString
      getCurrencyCodeBytes() {
    Object ref = currencyCode_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      currencyCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int UNITS_FIELD_NUMBER = 2;
  private long units_;
  /**
   * <pre>
   * The whole units of the amount.
   * For example if `currencyCode` is `"USD"`, then 1 unit is one US dollar.
   * </pre>
   *
   * <code>int64 units = 2;</code>
   * @return The units.
   */
  @Override
  public long getUnits() {
    return units_;
  }

  public static final int NANOS_FIELD_NUMBER = 3;
  private int nanos_;
  /**
   * <pre>
   * Number of nano (10^-9) units of the amount.
   * The value must be between -999,999,999 and +999,999,999 inclusive.
   * If `units` is positive, `nanos` must be positive or zero.
   * If `units` is zero, `nanos` can be positive, zero, or negative.
   * If `units` is negative, `nanos` must be negative or zero.
   * For example $-1.75 is represented as `units`=-1 and `nanos`=-750,000,000.
   * </pre>
   *
   * <code>int32 nanos = 3;</code>
   * @return The nanos.
   */
  @Override
  public int getNanos() {
    return nanos_;
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(currencyCode_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, currencyCode_);
    }
    if (units_ != 0L) {
      output.writeInt64(2, units_);
    }
    if (nanos_ != 0) {
      output.writeInt32(3, nanos_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(currencyCode_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, currencyCode_);
    }
    if (units_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, units_);
    }
    if (nanos_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, nanos_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof Money)) {
      return super.equals(obj);
    }
    Money other = (Money) obj;

    if (!getCurrencyCode()
        .equals(other.getCurrencyCode())) return false;
    if (getUnits()
        != other.getUnits()) return false;
    if (getNanos()
        != other.getNanos()) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + CURRENCY_CODE_FIELD_NUMBER;
    hash = (53 * hash) + getCurrencyCode().hashCode();
    hash = (37 * hash) + UNITS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getUnits());
    hash = (37 * hash) + NANOS_FIELD_NUMBER;
    hash = (53 * hash) + getNanos();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static Money parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Money parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Money parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Money parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Money parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Money parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Money parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Money parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static Money parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static Money parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static Money parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Money parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(Money prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Represents an amount of money with its currency type.
   * </pre>
   *
   * Protobuf type {@code google.type.Money}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.type.Money)
      MoneyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MoneyProto.internal_static_google_type_Money_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MoneyProto.internal_static_google_type_Money_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Money.class, Builder.class);
    }

    // Construct using com.google.type.Money.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      currencyCode_ = "";

      units_ = 0L;

      nanos_ = 0;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return MoneyProto.internal_static_google_type_Money_descriptor;
    }

    @Override
    public Money getDefaultInstanceForType() {
      return Money.getDefaultInstance();
    }

    @Override
    public Money build() {
      Money result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public Money buildPartial() {
      Money result = new Money(this);
      result.currencyCode_ = currencyCode_;
      result.units_ = units_;
      result.nanos_ = nanos_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof Money) {
        return mergeFrom((Money)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(Money other) {
      if (other == Money.getDefaultInstance()) return this;
      if (!other.getCurrencyCode().isEmpty()) {
        currencyCode_ = other.currencyCode_;
        onChanged();
      }
      if (other.getUnits() != 0L) {
        setUnits(other.getUnits());
      }
      if (other.getNanos() != 0) {
        setNanos(other.getNanos());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Money parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (Money) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object currencyCode_ = "";
    /**
     * <pre>
     * The three-letter currency code defined in ISO 4217.
     * </pre>
     *
     * <code>string currency_code = 1;</code>
     * @return The currencyCode.
     */
    public String getCurrencyCode() {
      Object ref = currencyCode_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        currencyCode_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * The three-letter currency code defined in ISO 4217.
     * </pre>
     *
     * <code>string currency_code = 1;</code>
     * @return The bytes for currencyCode.
     */
    public com.google.protobuf.ByteString
        getCurrencyCodeBytes() {
      Object ref = currencyCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        currencyCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * The three-letter currency code defined in ISO 4217.
     * </pre>
     *
     * <code>string currency_code = 1;</code>
     * @param value The currencyCode to set.
     * @return This builder for chaining.
     */
    public Builder setCurrencyCode(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      currencyCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The three-letter currency code defined in ISO 4217.
     * </pre>
     *
     * <code>string currency_code = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCurrencyCode() {
      
      currencyCode_ = getDefaultInstance().getCurrencyCode();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The three-letter currency code defined in ISO 4217.
     * </pre>
     *
     * <code>string currency_code = 1;</code>
     * @param value The bytes for currencyCode to set.
     * @return This builder for chaining.
     */
    public Builder setCurrencyCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      currencyCode_ = value;
      onChanged();
      return this;
    }

    private long units_ ;
    /**
     * <pre>
     * The whole units of the amount.
     * For example if `currencyCode` is `"USD"`, then 1 unit is one US dollar.
     * </pre>
     *
     * <code>int64 units = 2;</code>
     * @return The units.
     */
    @Override
    public long getUnits() {
      return units_;
    }
    /**
     * <pre>
     * The whole units of the amount.
     * For example if `currencyCode` is `"USD"`, then 1 unit is one US dollar.
     * </pre>
     *
     * <code>int64 units = 2;</code>
     * @param value The units to set.
     * @return This builder for chaining.
     */
    public Builder setUnits(long value) {
      
      units_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The whole units of the amount.
     * For example if `currencyCode` is `"USD"`, then 1 unit is one US dollar.
     * </pre>
     *
     * <code>int64 units = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearUnits() {
      
      units_ = 0L;
      onChanged();
      return this;
    }

    private int nanos_ ;
    /**
     * <pre>
     * Number of nano (10^-9) units of the amount.
     * The value must be between -999,999,999 and +999,999,999 inclusive.
     * If `units` is positive, `nanos` must be positive or zero.
     * If `units` is zero, `nanos` can be positive, zero, or negative.
     * If `units` is negative, `nanos` must be negative or zero.
     * For example $-1.75 is represented as `units`=-1 and `nanos`=-750,000,000.
     * </pre>
     *
     * <code>int32 nanos = 3;</code>
     * @return The nanos.
     */
    @Override
    public int getNanos() {
      return nanos_;
    }
    /**
     * <pre>
     * Number of nano (10^-9) units of the amount.
     * The value must be between -999,999,999 and +999,999,999 inclusive.
     * If `units` is positive, `nanos` must be positive or zero.
     * If `units` is zero, `nanos` can be positive, zero, or negative.
     * If `units` is negative, `nanos` must be negative or zero.
     * For example $-1.75 is represented as `units`=-1 and `nanos`=-750,000,000.
     * </pre>
     *
     * <code>int32 nanos = 3;</code>
     * @param value The nanos to set.
     * @return This builder for chaining.
     */
    public Builder setNanos(int value) {
      
      nanos_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Number of nano (10^-9) units of the amount.
     * The value must be between -999,999,999 and +999,999,999 inclusive.
     * If `units` is positive, `nanos` must be positive or zero.
     * If `units` is zero, `nanos` can be positive, zero, or negative.
     * If `units` is negative, `nanos` must be negative or zero.
     * For example $-1.75 is represented as `units`=-1 and `nanos`=-750,000,000.
     * </pre>
     *
     * <code>int32 nanos = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearNanos() {
      
      nanos_ = 0;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.type.Money)
  }

  // @@protoc_insertion_point(class_scope:google.type.Money)
  private static final Money DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new Money();
  }

  public static Money getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Money>
      PARSER = new com.google.protobuf.AbstractParser<Money>() {
    @Override
    public Money parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Money(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Money> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Money> getParserForType() {
    return PARSER;
  }

  @Override
  public Money getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: date.proto

package com.google.type;

public final class DateProto {
  private DateProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_type_Date_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_type_Date_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\ndate.proto\022\013google.type\"0\n\004Date\022\014\n\004yea" +
      "r\030\001 \001(\005\022\r\n\005month\030\002 \001(\005\022\013\n\003day\030\003 \001(\005B]\n\017c" +
      "om.google.typeB\tDateProtoP\001Z4google.gola" +
      "ng.org/genproto/googleapis/type/date;dat" +
      "e\370\001\001\242\002\003GTPb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_google_type_Date_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_type_Date_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_type_Date_descriptor,
        new String[] { "Year", "Month", "Day", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

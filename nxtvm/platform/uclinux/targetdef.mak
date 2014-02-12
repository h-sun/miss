#
# This file defines the source and target file names
#

# TARGET is the base name for the outputs.
# C_RAMSOURCES are the C files that must always be located in RAM.(removed)
# C_SOURCES are the rest of the C files
# S_SOURCES are the assembler files(removed)

VM_DIR := ../../javavm
VM_PREFIX := jvm_

TARGET := lejos_uclinux

C_HOOK_OBJECTS := \
	tvmemul.o \
	nativeemul.o \
	sensors.o \
	load.o \
	traceemul.o \
    verbose.o \
    opcodeinfo.o \
	platform_hooks.o \
	syscom.o \
	wireless.o \
	fileoperation.o

C_VM_OBJECTS := \
	$(VM_PREFIX)interpreter.o \
	$(VM_PREFIX)threads.o \
	$(VM_PREFIX)exceptions.o \
	$(VM_PREFIX)memory.o \
	$(VM_PREFIX)language.o \
	$(VM_PREFIX)poll.o \
	$(VM_PREFIX)debug.o

C_OBJECTS := \
	$(C_HOOK_OBJECTS) \
	$(C_VM_OBJECTS)


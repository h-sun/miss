.SUFFIXES:

default: all

include environment.mak
include targetdef.mak


#C_OPTIMISATION_FLAGS = -Os
#C_OPTIMISATION_FLAGS = -Os -Xassembler -aslh
#C_OPTIMISATION_FLAGS = -O0


CFLAGS = $(BASE_ABI_FLAGS) -mthumb \
	#-ffreestanding -fsigned-char \   #程序语言选项
	#$(C_OPTIMISATION_FLAGS) -g \
	#-Wall -Winline -Werror-implicit-function-declaration \   #编译时的警告选项
	-I. -I$(VM_DIR) \


ALL_ELF := $(TARGET)
ALL_OBJECTS := $(C_OBJECTS)
ALL_HEAD := $(VM_DIR)/specialclasses.h $(VM_DIR)/specialsignatures.h

.SECONDARY: $(ALL_ELF) $(ALL_OBJECTS) $(ALL_HEAD)

.PHONY: header
header: $(ALL_HEAD)

.PHONY: TargetMessage
TargetMessage:
	@echo ""
	@echo "Building: $(ALL_TARGETS)"
	@echo ""
	@echo "C objects: $(C_OBJECTS)"
	@echo ""

.PHONY: BuildMessage
BuildMessage: TargetMessage EnvironmentMessage

.PHONY: clean
clean:
	@echo "Removing All Objects"
	@rm -f $(ALL_OBJECTS)
	@echo "Removing target"
	@rm -f $(ALL_ELF)


#%.elf.map %.elf: %.ld $(C_OBJECTS) $(S_OBJECTS)
#	@echo "Linking $@ using linker script $<"
#	$(LD) $(LDFLAGS) -T $< -Map $@.map -o $@ $(C_OBJECTS) $(S_OBJECTS) $(LIBM) $(LIBC) $(LIBGCC)

#%.bin: %.elf
#	@echo "Generating binary file $@ from $<"
#	$(OBJCOPY) -O binary $< $@

$(TARGET): $(C_OBJECTS)
    @echo "Generating the exec file"
    $(CC) -o $(C_OBJECTS) $(LIBM) $(LIBC) $(LIBGCC)
# generated headers:

#$(VM_DIR)/specialclasses.h: $(VM_DIR)/specialclasses.db
#	../../dbtoh.sh class $< $@

#$(VM_DIR)/specialsignatures.h: $(VM_DIR)/specialsignatures.db
#	../../dbtoh.sh signature $< $@


# default rules for compiling sources

%.o: %.c $(ALL_HEAD)
	@echo "Compiling $< to $@"
	$(CC) $(CFLAGS) -c -o $@ $<


### special rules for compiling JVM sources

$(VM_PREFIX)%.o: $(VM_DIR)/%.c $(ALL_HEAD)
	@echo "Compiling $< to $@"
	$(CC) $(CFLAGS) -c -o $@ $<

$(VM_PREFIX)interpreter.o: $(VM_DIR)/interpreter.c $(ALL_HEAD)
	@echo "Compiling $< to $@"
	$(CC) $(CFLAGS) -O3 -c -o $@ $<


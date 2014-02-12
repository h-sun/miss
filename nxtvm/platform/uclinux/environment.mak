LEJOS_ARM_GCC := arm-linux-gcc

BASE_ABI_FLAGS := -msoft-float
LIB_ABI_FLAGS  := $(BASE_ABI_FLAGS) -mthumb
#-mcpu  -mcpu=arm7tdmi cpu的型号
#-mlittle-endian 产生运行在little-endian模式下的处理器代码。这是所有标准配置的缺省情况
#-mfloat-abi=soft 软件形式处理浮点运算？
#-mthumb-interwork(BASE_ABI_FLAGS) 产生支持THUMB和ARM指令集间调用的代码。
#-mthumb(LIB_ABI_FLAGS)

MACRO_PROG_PATH = $(abspath $(shell "$(CC)" -print-prog-name="$(1)"))
MACRO_LIB_PATH  = $(abspath $(shell "$(CC)" $(LIB_ABI_FLAGS) -print-file-name="$(1)"))

CC        := $(LEJOS_ARM_GCC)
LD        := $(call MACRO_PROG_PATH,ld)
OBJCOPY   := $(call MACRO_PROG_PATH,objcopy)
OBJDUMP   := $(call MACRO_PROG_PATH,objdump)

LIBGCC    := $(call MACRO_LIB_PATH,libgcc.a)
LIBC      := $(call MACRO_LIB_PATH,libc.a)
LIBM      := $(call MACRO_LIB_PATH,libm.a)

.PHONY: EnvironmentMessage
EnvironmentMessage:
	@echo " CC      $(CC)"
	@echo " LD      $(LD)"
	@echo " OBJCOPY $(OBJCOPY)"
	@echo " OBJDUMP $(OBJDUMP)"
	@echo " LIBGCC  $(LIBGCC)"
	@echo " LIBC    $(LIBC)"
	@echo " LIBM    $(LIBM)"
	@echo ""


JAVA_HOME=/usr/lib/java
#  mozilla dist folder
MOZ_HOME=/home/builder/projects/mozilla/mozilla-source-1.6/objs-i686-pc-linux-gnu-VPE/dist

# Define the shared library to be made.
VPE_PREFIX=vpednd
VPE_LIB=lib$(VPE_PREFIX).so
DND_OBJS=DragListener.o VpeDnD.o VpeDnDModule.o

MOZ_REQ_LIB = -lstring_s -lstring_obsolete_s -lembed_base_s -lunicharutil_s -lnspr4 -lxpcom -lplds4 -lgkgfx
LDLIBS=-L$(MOZ_HOME)/lib $(MOZ_REQ_LIB)

OUT_DIR=./bin/linux

MOZ_INCLUDES =  -I$(MOZ_HOME) \
	-I$(MOZ_HOME)/include/xpcom \
	-I$(MOZ_HOME)/include/string \
	-I$(MOZ_HOME)/include/nspr \
	-I$(MOZ_HOME)/include/embed_base \
	-I$(MOZ_HOME)/include/dom \
	-I$(MOZ_HOME)/include/content \
	-I$(MOZ_HOME)/include/widget \
	-I$(MOZ_HOME)/include/layout \
	-I$(MOZ_HOME)/include/necko \
	-I$(MOZ_HOME)/include/view \
	-I$(MOZ_HOME)/include/locale \
	-I$(MOZ_HOME)/include/xuldoc \
	-I$(MOZ_HOME)/include/gfx
# -O 
#	-D_DEBUG=1\

CFLAGS = \
	-O \
	-fshort-wchar\
	-fPIC \
	-fno-rtti	\
	-Wall	\
	-I./ \
	-I$(JAVA_HOME)/include	\
	-I$(JAVA_HOME)/include/linux	\
	-include $(MOZ_HOME)/include/mozilla-config.h \
	$(MOZ_INCLUDES)

# Default location of mozilla libraries for supported versions
#LDFLAGS_DEFAULT_MOZILLA_PATH = -Xlinker -rpath -Xlinker /usr/lib/mozilla-1.4

LDFLAGS =-s -shared -fPIC 

all: $(VPE_LIB)

$(VPE_LIB): $(DND_OBJS)
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

#*.o: *.cpp
#	$(CXX) -c $(CFLAGS) $< -o $(OUT_DIR)/$@

DragListener.o: DragListener.cpp DragListener.h IVpeDragDropListener.h
	$(CXX) -c $(CFLAGS) $< -o $@

VpeDnDModule.o: VpeDnDModule.cpp IVpeDnD.h
	$(CXX) -c $(CFLAGS) $< -o $@

VpeDnD.o: VpeDnD.cpp VpeDnD.h IVpeDnD.h
	$(CXX) -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o *.so


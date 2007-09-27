JAVA_HOME=/usr/lib/java
#  mozilla dist folder
MOZ_HOME=/home/builder/projects/mozilla/mozilla-source-1.6/objs-i686-pc-linux-gnu-VPE/dist

# Define the shared library to be made.
WS_PREFIX=gtk
VPE_PREFIX=vpe-mozilla
VPE_OBJ=$(VPE_PREFIX).o
VPE_LIB=lib$(VPE_PREFIX)-$(WS_PREFIX).so

MOZ_REQ_LIB = -lstring_s -lstring_obsolete_s -lembed_base_s -lunicharutil_s -lnspr4 -lxpcom -lplds4
LDLIBS=-L$(MOZ_HOME)/lib $(MOZ_REQ_LIB)

MOZ_INCLUDES =  -I$(MOZ_HOME)/include \
	-I$(MOZ_HOME)/include/xpcom \
	-I$(MOZ_HOME)/include/string \
	-I$(MOZ_HOME)/include/nspr \
	-I$(MOZ_HOME)/include/embed_base \
	-I$(MOZ_HOME)/include/gfx

CFLAGS = -O \
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

LDFLAGS = -s -shared -fPIC 
#$(LDFLAGS_DEFAULT_MOZILLA_PATH)

all: $(VPE_LIB)

$(VPE_LIB): $(VPE_OBJ)
	$(CXX) $(LDFLAGS) -o $@ $^ $(LDLIBS)

$(VPE_OBJ): xpcom.cpp
	$(CXX) -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o *.so


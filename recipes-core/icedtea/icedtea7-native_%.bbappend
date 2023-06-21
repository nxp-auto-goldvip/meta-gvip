ICEDTEA_HG_MIRROR = "http://icedtea.wildebeest.org/hg/release/${ICEDTEA_PREFIX}"

# Remove the links that use the old webserver
SRC_URI:remove = " \
    ${OPENJDK_URI} \
    ${HOTSPOT_URI} \
    ${CORBA_URI} \
    ${JAXP_URI} \
    ${JAXWS_URI} \
    ${JDK_URI} \
    ${LANGTOOLS_URI} \
"

SRC_URI:prepend = " \
    ${ICEDTEA_HG_MIRROR}/archive/${OPENJDK_FILE_UPSTREAM};name=openjdk;unpack=false;downloadfilename=${OPENJDK_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/hotspot/archive/${HOTSPOT_FILE_UPSTREAM};name=hotspot;unpack=false;downloadfilename=${HOTSPOT_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/corba/archive/${CORBA_FILE_UPSTREAM};name=corba;unpack=false;downloadfilename=${CORBA_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/jaxp/archive/${JAXP_FILE_UPSTREAM};name=jaxp;unpack=false;downloadfilename=${JAXP_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/jaxws/archive/${JAXWS_FILE_UPSTREAM};name=jaxws;unpack=false;downloadfilename=${JAXWS_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/jdk/archive/${JDK_FILE_UPSTREAM};name=jdk;unpack=false;downloadfilename=${JDK_FILE_DOWNLOAD} \
    ${ICEDTEA_HG_MIRROR}/langtools/archive/${LANGTOOLS_FILE_UPSTREAM};name=langtools;unpack=false;downloadfilename=${LANGTOOLS_FILE_DOWNLOAD} \
"

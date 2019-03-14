DESCRIPTION = "A X11 image to run tensorflow demo"
LICENSE = "MIT"

require recipes-graphics/images/core-image-x11.bb

IMAGE_FEATURES += "ssh-server-dropbear"

IMAGE_INSTALL += " \
    dhcp-client \
    face-detection \
"

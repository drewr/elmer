LEIN ?= lein
NAME = elmer
VERSION = $(shell git ver)
JAR = $(NAME)-$(VERSION).jar

package: target/$(JAR)

clean:
	$(LEIN) clean

target/$(JAR):
	echo $(VERSION) >version.txt
	LEIN_SNAPSHOTS_IN_RELEASE=yes $(LEIN) uberjar


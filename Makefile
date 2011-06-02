PROJECT = elmer
WAR = paste
DEST = deploy@draines
JETTY_PATH = /apps/jetty
LOCAL_JETTY_PATH = ~/src/jetty

build:
	lein clean && lein deps && lein compile && lein uberwar

local-deploy:
	lein clean && lein deps && lein compile && lein uberwar \
		&& mv $(PROJECT).war jetty/webapps/$(WAR).war \
		&& touch jetty/contexts/$(WAR).xml \
		&& ( cd jetty; tar cf - . ) \
		| ( cd $(LOCAL_JETTY_PATH); tar xvf - )

deploy: build
	mv $(PROJECT).war jetty/webapps/$(WAR).war \
		&& touch jetty/contexts/$(WAR).xml \
		&& ( cd jetty; tar cf - . ) \
		| ( ssh $(DEST) cd $(JETTY_PATH)\; tar xvf - )

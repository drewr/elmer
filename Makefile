deploy:
	lein clean && lein deps && lein compile && lein uberwar \
		&& mv elmer-*.war jetty/webapps/paste.war \
		&& touch jetty/contexts/paste.xml \
		&& ( cd jetty; tar cf - . ) \
		| ( ssh deploy@draines cd /apps/jetty\; tar xvf - )

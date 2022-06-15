FROM jboss/keycloak:16.1.1

# Make the realm configuration available for import
COPY kcrealm.json /opt/keycloak_import/

# Import the realm and user
RUN /opt/keycloak/bin/kc.sh import --file /opt/keycloak_import/kcrealm.json

# The Keycloak server is configured to listen on port 8180, access it on localhost:8180
EXPOSE 8180/tcp

# Import the realm on start-up
CMD ["start-dev"]
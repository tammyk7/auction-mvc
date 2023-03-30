#!/bin/sh
set -eo pipefail

echo "*** Starting AdminUI server..."

echo "*** Substitute the variables ***"
envsubst '\${FRONTEND_URL}' < /etc/nginx/nginx.conf.tmpl > /etc/nginx/nginx.conf
exec nginx -g 'daemon off;' "$@"

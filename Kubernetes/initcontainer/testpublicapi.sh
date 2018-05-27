#!/bin/bash

if curl --output /dev/null --silent --head --fail "https://api.lyrics.ovh/v1/coldplay/paradise"; then
  echo "Public API exists: $url"
else
  echo "Public API does not exist: $url"
fi


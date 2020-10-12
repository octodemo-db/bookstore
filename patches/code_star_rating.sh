#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

git checkout -b feature-book-star-rating
git apply $DIR/star_rating/star_rating.diff
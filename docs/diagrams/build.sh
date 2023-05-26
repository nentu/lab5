#!/bin/bash

rm -rf ./img/*
plantuml -o ./../img -tsvg ./puml/*.puml
plantuml -o ./../img ./puml/*.puml

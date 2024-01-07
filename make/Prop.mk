#### --------------- ======================================================= ####
####  :: Prop.mk ::   Provides necessary project information and properties  ####
#### --------------- ======================================================= ####

# Copyright (c) 2023 Ryuu Mitsuki
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Define a guard variable to prevent and protect from import recursion
ifndef __MAKE_PROP_MK
__MAKE_PROP_MK = 1

RETRIEVER := scripts/retriever.py

### Retrieve required project information
ifeq ($(call __is_exist,$(RETRIEVER)),1)
  # Project information
  PROGNAME        := $(shell $(PY) $(RETRIEVER) project.name)
  VERSION         := $(shell $(PY) $(RETRIEVER) project.version)
  GROUP_ID        := $(shell $(PY) $(RETRIEVER) project.groupId)
  ARTIFACT_ID     := $(shell $(PY) $(RETRIEVER) project.artifactId)
  AUTHOR          := $(shell $(PY) $(RETRIEVER) 'project.developers[0].name')
  AUTHOR_URL      := $(shell $(PY) $(RETRIEVER) 'project.developers[0].url')
  INCEPTION_YEAR  := $(shell $(PY) $(RETRIEVER) project.inceptionYear)
  LICENSE         := $(shell $(PY) $(RETRIEVER) 'project.licenses[0].name')
  LICENSE_URL     := $(shell $(PY) $(RETRIEVER) 'project.licenses[0].url')
else
  $(call __raise_err,Fatal,No such file or directory: $(RETRIEVER))
endif  # __is_exist : $(RETRIEVER)

endif  # __MAKE_PROP_MK

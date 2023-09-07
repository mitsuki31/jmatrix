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


PROGNAME        := jmatrix
VERSION         := 1.2.2
AUTHOR          := Ryuu Mitsuki
INCEPTION_YEAR  := 2023

endif  # __MAKE_PROP_MK
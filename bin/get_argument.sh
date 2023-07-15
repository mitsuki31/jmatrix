#!/usr/bin/bash

#################################################################
# This script processes command-line arguments and searches
# for a specific argument in the list. It supports an
# optional "-s" switch to specify a specific argument.
# The script outputs the index of the desired argument in
# the modified argument list.
#################################################################

# Usage function to display instructions on how to use the script
usage() {
    echo -e "\033[1mUSAGE\033[0m"
    echo "   $ . get_argument.sh [OPTIONS] ARGUMENTS"
    echo "   $ bash get_argument.sh [OPTIONS] ARGUMENTS"
    echo -e "\n\033[1mDESCRIPTION\033[0m"
    echo "   This script processes command-line arguments and searches "
    echo "   for a specific argument in the list, then outputs the index"
    echo "   of the desired argument."
    echo -e "\n\033[1mOPTIONS\033[0m"
    echo "   -s ARG          Specify a specific argument to search for."
    echo "   -v, --verbose   Enable verbose output (saved in \"tmp/\")."
    echo "   -h, --help      Show this help message."
    echo -e "\n\033[1mAUTHOR\033[0m"
    echo "   Ryuu Mitsuki"
}


args=("$@")         # Store the command-line arguments in an array
length=${#args[@]}  # Get the total number of arguments
wanted_arg=""

new_args=()         # Initialize an empty array for the modified arguments

skip_next=0         # Variable to skip the argument following the "-s" switch
verbose=0

curdir=`pwd`
tmpfile=$curdir/tmp/index.tmp
log=$curdir/tmp/get_argument.log

[ -f $log ] && > $log   # Clear the log file if exist

info() {
    [ $1 = "Info" ] && echo -ne "[\033[1;92m$1\033[0m]${@/$1}\n" &>>$log
    [ $1 = "Warning" ] && echo -ne "[\033[1;93m$1\033[0m]${@/$1}\n" &>>$log
}

# Print usage if no arguments givem
[ $# -eq 0 ] && usage && return 0

for (( i=0; i < $length; i++ )); do
    arg="${args[i]}"

    if [ $skip_next -eq 1 ]; then
        skip_next=0
        continue
    fi

    case $arg in
        -s)
            if (( i+1 < $length )); then
                skip_next=1
                wanted_arg="${args[i+1]}"
                continue
            else
                echo "Error: Switch '-s' need an argument"
                return -1
            fi
            ;;

        -v | --verbose)
            verbose=1

            # Only create temporary directory if the
            # verbose activated
            mkdir -p `dirname $tmpfile`
            continue
            ;;

        -h | --help)
            usage
            return 0
            ;;
    esac

    new_args+=("$arg")
done
unset arg args i skip_next length

if [ $verbose -eq 1 ]; then
    info "Info" "Arguments       : ${new_args[@]} (${#new_args[@]})"
    info "Info" "Wanted argument : $wanted_arg"
fi

found_index=-1  # Initialize the index of the desired argument as -1

for (( i=0; i < ${#new_args[@]}; i++ )); do
    arg="${new_args[i]}"

    if [ $arg = $wanted_arg ]; then
        found_index=$i
        break
    fi
done
unset arg i new_args

if [ $verbose -eq 1 ]; then
    echo &>>$log
    info "Info" "Index of '$wanted_arg': $found_index"
    if [ $found_index -eq -1 ]; then
        info "Warning" "Index -1 means the argument not found"
    fi
fi

echo $(( $found_index + 1 ))
unset found_index curdir tmpfile verbose info log

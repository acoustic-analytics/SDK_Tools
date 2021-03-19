#--------------------------------------------------------------------------------------------
# Copyright (C) 2017 Acoustic, L.P. All rights reserved.
#
# NOTICE: This file contains material that is confidential and proprietary to
# Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
# industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
# Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
# prohibited.
#--------------------------------------------------------------------------------------------

require 'rubygems'
require 'json'
require 'pp'
require 'date'

class JsonUtil
  attr_accessor :file, :hash

  def initialize(file,jsonString)
    @file = file

    if File::exists?(file)
      json = File.read(file)
      @hash = JSON.parse(json)
    else
      @hash = JSON.parse(jsonString)
      save
    end
  end

  def print
    puts JSON.pretty_generate @hash
  end

  def add(hashPath, value = nil)
    return unless hashPath.length > 0
    index = hashPath
    @hash[index] = value
  end

  def get(hashPath)
    return unless hashPath.length > 0
    index = hashPath
    return @hash[index]
  end

  def save
    year = Date.today.year
    copyright = "/*******************************************************************************\n" +
                "* Licensed Materials - Property of IBM\n" +
                "* (C) Copyright IBM Corp. #{year}\n" +
                "* US Government Users Restricted Rights - Use, duplication or disclosure\n" +
                "* restricted by GSA ADP Schedule Contract with IBM Corp.\n" +
                "******************************************************************************/\n"

    file = File.new(@file, File::CREAT|File::TRUNC|File::RDWR, 0644)
    file.write(copyright)
    file.close
    file = File.new(@file, "a")
    file.puts(JSON.pretty_generate(@hash, :indent => "  "))
    file.close
  end
end

#ifndef _COMMON_H
#define _COMMON_H

#include <vector>
#include <string>
#include "resource.h"

extern std::vector< std::string > LOG;

#define DEBUG(x) LOG.push_back(x)

#endif

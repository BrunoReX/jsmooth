#ifndef __VERSION_H_
#define __VERSION_H_

#include <string>

#include "common.h"
#include "StringUtils.h"

class Version
{
private:
    int m_major;
    int m_minor;
    int m_sub;

public:
    std::string Value;
    
    Version(std::string val);
    Version();
    int getMajor() const;
    int getMinor() const;
    int getSubMinor() const;

    std::string toString() const;

//    bool operator > (const Version& v) const;

    friend bool operator < (const Version& v1, const Version& v2);
    friend bool operator <= (const Version& v1, const Version& v2);
private:
    void parseValue(const std::string& val);

    int extractIntAt(const std::string& val, int pos) const;
    
public:
    struct AscendingSort
    {
         bool operator()(const Version& v1, const Version& v2)
        {
            if (v1.getMajor() > v2.getMajor())
                return true;
            if (v1.getMajor() < v2.getMajor())
                return false;
                
             if (v1.getMinor() > v2.getMinor())
                  return true;
             if (v1.getMinor() < v2.getMinor())
                  return false;
        
             if (v1.getSubMinor() > v2.getSubMinor())
                  return true;
             if (v1.getSubMinor() < v2.getSubMinor())
                  return false;
                  
             return false;
        }
    };

};



#endif

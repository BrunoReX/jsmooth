#ifndef __VERSION_H_
#define __VERSION_H_

#include <string>

class Version
{
public:
    std::string Value;
    
    Version(std::string val);
    Version();
    int getMajor() const;
    int getMinor() const;
    int getSubMinor() const;

//    bool operator > (const Version& v) const;

    friend bool operator < (const Version& v1, const Version& v2);
private:
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

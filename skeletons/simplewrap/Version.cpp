#include "Version.h"

Version::Version(std::string val)
{
    Value = val;
}

Version::Version()
{
}

int Version::getMajor() const
{
    return extractIntAt(Value, 0);
}
    
int Version::getMinor() const
    {
        int pos = Value.find(".", 0);
        if (pos != std::string::npos)
        {
            return extractIntAt(Value, pos+1);
        }
        return 0;
    }

int Version::getSubMinor() const
{
     int pos = Value.find(".", 0);
     if (pos != std::string::npos)
        {
                int pos2 = Value.find(".", pos+1);
                if (pos2 != std::string::npos)
                {
                     return extractIntAt(Value, pos2);
                }
        }
        return 0;
}


int Version::extractIntAt(const std::string& val, int pos) const
{
    if (pos == std::string::npos)
            return pos;
            
    std::string tmp("");
    for (int i=pos; (i != std::string::npos) && (i<val.length()) && (val[i]!='.'); i++)
    {
            tmp += val[i];
    }
    return atoi(tmp.c_str());
 }    


bool operator < (const Version& v1, const Version& v2)
{
    if (v1.getMajor() < v2.getMajor())
        return true;
    if (v1.getMajor() > v2.getMajor())
        return false;
        
     if (v1.getMinor() < v2.getMinor())
          return true;
     if (v1.getMinor() > v2.getMinor())
          return false;

     if (v1.getSubMinor() < v2.getSubMinor())
          return true;
     if (v1.getSubMinor() > v2.getSubMinor())
          return false;
          
     return false;
}



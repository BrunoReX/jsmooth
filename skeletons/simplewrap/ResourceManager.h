
#ifndef RESOURCEMANAGER_H
#define RESOURCEMANAGER_H

#include <windows.h>
#include <string>
#include <vector>

class ResourceManager
{
private:
    std::string m_mainName;
    std::string m_resourceCategory;
    int m_resourceMainId;
    int m_resourceJarId;
    std::string m_lastError;    
    HGLOBAL m_jarHandler;
    int m_jarSize;
public:

    ResourceManager(std::string category, int mainNameId, int jarId);
    void saveTemp(std::string tempname);
    std::string getMainName();
    std::string getLastErrorString()
    {
        return m_lastError;
    }
    
    std::string idToResourceName(int id)
    {
        char buffer[32];
        sprintf(buffer, "%d", id);
        std::string result("#");
        result += buffer;
        return result;
    }
    
};


#endif


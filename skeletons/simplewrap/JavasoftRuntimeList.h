#include <windows.h>
#include <string>
#include <vector>

#include <jni.h>

class JavasoftVM
{
    public:
    std::string Path;
    std::string Version;
};

class JavasoftRuntimeList
{
private:
    std::vector<JavasoftVM> m_jvms;

public:
    std::string Message;
    
  JavasoftRuntimeList();
  
  int getCount()
  {
    return m_jvms.size();
  }
  
  const JavasoftVM& getAt(int i)
  {
    return m_jvms.at(i);
  }

  void run();
//  void run2();

  void copyString(const std::string& str, char* buffer, int max)
  {
       int size = str.size();
       if (size+2 > max)
              size = max-2;
	   str.copy(buffer, size);
	   buffer[size] = 0;
  }

};

/*
 * java-tron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * java-tron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.tron.application;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class ApplicationFactory {

  /**
   * Guice是由Google大牛Bob lee开发的一款绝对轻量级的java IoC容器。其优势在于：
   * 速度快，号称比spring快100倍。
   * 无外部配置(如需要使用外部可以可以选用Guice的扩展包)，完全基于annotation特性，支持重构，代码静态检查。
   * 简单，快速，基本没有学习成本。
   * Build a Guice instance
   *
   * @return Guice
   */
  public Injector buildGuice() {
    return Guice.createInjector(
        new Module());
  }

  /**
   * Build a new application
   *
   * @return
   */
  public Application build() {
    return new Application(buildGuice());
  }

  /**
   * Build a new cli application
   *
   * @return
   */
  public CliApplication buildCli() {
    return new CliApplication(buildGuice());
  }
}
